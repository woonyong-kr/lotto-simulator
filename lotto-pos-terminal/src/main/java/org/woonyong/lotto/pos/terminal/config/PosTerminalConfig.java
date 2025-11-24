package org.woonyong.lotto.pos.terminal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pos.terminal")
public class PosTerminalConfig {
    private String centralServerUrl;
    private String posManagerUrl;
    private int statisticsReportInterval;
    private int connectionTimeout;
    private int readTimeout;
    private int healthCheckIntervalMs;
    private int ownerValidationFailureThreshold;

    public String getCentralServerUrl() {
        return centralServerUrl;
    }

    public void setCentralServerUrl(final String centralServerUrl) {
        this.centralServerUrl = centralServerUrl;
    }

    public String getPosManagerUrl() {
        return posManagerUrl;
    }

    public void setPosManagerUrl(final String posManagerUrl) {
        this.posManagerUrl = posManagerUrl;
    }

    public int getStatisticsReportInterval() {
        return statisticsReportInterval;
    }

    public void setStatisticsReportInterval(final int statisticsReportInterval) {
        this.statisticsReportInterval = statisticsReportInterval;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }


    public int getHealthCheckIntervalMs() {
        return healthCheckIntervalMs;
    }

    public void setHealthCheckIntervalMs(final int healthCheckIntervalMs) {
        this.healthCheckIntervalMs = healthCheckIntervalMs;
    }

    public int getOwnerValidationFailureThreshold() {
        return ownerValidationFailureThreshold;
    }

    public void setOwnerValidationFailureThreshold(final int ownerValidationFailureThreshold) {
        this.ownerValidationFailureThreshold = ownerValidationFailureThreshold;
    }
}