package com.pragma.reconciliation.infrastructure.config;

public class ReconciliationConfig {
    private MatchingWindow matchingWindow;
    private Tolerances tolerances;
    private Processing processing;
    private Alerts alerts;

    public MatchingWindow getMatchingWindow() {
        return matchingWindow;
    }

    public void setMatchingWindow(MatchingWindow matchingWindow) {
        this.matchingWindow = matchingWindow;
    }

    public Tolerances getTolerances() {
        return tolerances;
    }

    public void setTolerances(Tolerances tolerances) {
        this.tolerances = tolerances;
    }

    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Alerts getAlerts() {
        return alerts;
    }

    public void setAlerts(Alerts alerts) {
        this.alerts = alerts;
    }

    public int getSlaThresholdMinutes() {
        return alerts != null && alerts.getSlaThresholdMinutes() > 0 
            ? alerts.getSlaThresholdMinutes() 
            : 5;
    }

    public int getAlertCheckIntervalSeconds() {
        return alerts != null && alerts.getCheckIntervalSeconds() > 0 
            ? alerts.getCheckIntervalSeconds() 
            : 30;
    }

    public static class MatchingWindow {
        private int defaultMinutes = 60;
        private int maxMinutes = 1440;

        public int getDefaultMinutes() {
            return defaultMinutes;
        }

        public void setDefaultMinutes(int defaultMinutes) {
            this.defaultMinutes = defaultMinutes;
        }

        public int getMaxMinutes() {
            return maxMinutes;
        }

        public void setMaxMinutes(int maxMinutes) {
            this.maxMinutes = maxMinutes;
        }
    }

    public static class Tolerances {
        private double amountPercentage = 0.01;
        private double amountAbsolute = 0.01;

        public double getAmountPercentage() {
            return amountPercentage;
        }

        public void setAmountPercentage(double amountPercentage) {
            this.amountPercentage = amountPercentage;
        }

        public double getAmountAbsolute() {
            return amountAbsolute;
        }

        public void setAmountAbsolute(double amountAbsolute) {
            this.amountAbsolute = amountAbsolute;
        }
    }

    public static class Processing {
        private int batchSize = 100;
        private int maxConcurrent = 10;
        private int retryAttempts = 3;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getMaxConcurrent() {
            return maxConcurrent;
        }

        public void setMaxConcurrent(int maxConcurrent) {
            this.maxConcurrent = maxConcurrent;
        }

        public int getRetryAttempts() {
            return retryAttempts;
        }

        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }
    }

    public static class Alerts {
        private int slaThresholdMinutes = 5;
        private int checkIntervalSeconds = 30;
        private boolean enabled = true;

        public int getSlaThresholdMinutes() {
            return slaThresholdMinutes;
        }

        public void setSlaThresholdMinutes(int slaThresholdMinutes) {
            this.slaThresholdMinutes = slaThresholdMinutes;
        }

        public int getCheckIntervalSeconds() {
            return checkIntervalSeconds;
        }

        public void setCheckIntervalSeconds(int checkIntervalSeconds) {
            this.checkIntervalSeconds = checkIntervalSeconds;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}