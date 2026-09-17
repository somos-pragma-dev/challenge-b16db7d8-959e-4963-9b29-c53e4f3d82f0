package com.pragma.reconciliation.infrastructure.messaging;

import com.pragma.reconciliation.application.commands.ReconcileMovementCommand;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:reconciliation-processor}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit:false}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.max-poll-records:100}")
    private int maxPollRecords;

    @Value("${spring.kafka.consumer.max-poll-interval-ms:300000}")
    private int maxPollIntervalMs;

    @Value("${reconciliation.kafka.retry.max-attempts:3}")
    private int maxRetryAttempts;

    @Value("${reconciliation.kafka.retry.initial-interval-ms:1000}")
    private long initialIntervalMs;

    @Value("${reconciliation.kafka.retry.multiplier:2.0}")
    private double multiplier;

    @Value("${reconciliation.kafka.retry.max-interval-ms:10000}")
    private long maxIntervalMs;

    @Value("${reconciliation.kafka.topics.movements:banking.movements}")
    private String movementsTopic;

    @Value("${reconciliation.kafka.topics.reprocess:banking.movements.reprocess}")
    private String reprocessTopic;

    @Value("${reconciliation.kafka.topics.reconciled:banking.movements.reconciled}")
    private String reconciledTopic;

    @Value("${reconciliation.kafka.topics.dlq:banking.movements.dlq}")
    private String dlqTopic;

    @Bean
    public ConsumerFactory<String, ReconcileMovementCommand> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 30000);

        JsonDeserializer<ReconcileMovementCommand> deserializer = new JsonDeserializer<>(ReconcileMovementCommand.class);
        deserializer.addTrustedPackages("com.pragma.reconciliation.application.commands");
        deserializer.setUseTypeMapperForKey(true);
        deserializer.setTypeResolver(null);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ReconcileMovementCommand> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReconcileMovementCommand> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        factory.setCommonErrorHandler(createErrorHandler());
        factory.setRecordFilterStrategy(record -> {
            if (record.value() == null) {
                log.warn("Mensaje nulo recibido, filtrando");
                return true;
            }
            return false;
        });
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        return createErrorHandler();
    }

    private DefaultErrorHandler createErrorHandler() {
        ExponentialBackOff backOff = new ExponentialBackOff(initialIntervalMs, multiplier);
        backOff.setMaxInterval(maxIntervalMs);
        backOff.setMaxElapsedTime(maxRetryAttempts * (int) maxIntervalMs);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Error después de reintentos, enviando a DLQ: topic={}, key={}, error={}",
                            record.topic(), record.key(), exception.getMessage(), exception);
                },
                backOff
        );

        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                NullPointerException.class
        );

        return errorHandler;
    }

    @Bean
    public Map<String, Object> kafkaProducerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("retries", maxRetryAttempts);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        return props;
    }

    public String getMovementsTopic() {
        return movementsTopic;
    }

    public String getReprocessTopic() {
        return reprocessTopic;
    }

    public String getReconciledTopic() {
        return reconciledTopic;
    }

    public String getDlqTopic() {
        return dlqTopic;
    }
}