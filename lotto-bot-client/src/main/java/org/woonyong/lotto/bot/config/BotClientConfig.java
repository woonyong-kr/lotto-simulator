package org.woonyong.lotto.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotClientConfig {
    private int maxCapacity;
    private int defaultPurchaseIntervalMs;
    private int defaultTicketsPerPurchase;
    private int defaultPosCount;
    private int posFailureThreshold;
    private int healthCheckIntervalMs;
    private int sseIntervalMs;
    private int posReallocationRetryMs;
    private int posAllocationMaxRetry;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(final int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getDefaultPurchaseIntervalMs() {
        return defaultPurchaseIntervalMs;
    }

    public void setDefaultPurchaseIntervalMs(final int defaultPurchaseIntervalMs) {
        this.defaultPurchaseIntervalMs = defaultPurchaseIntervalMs;
    }

    public int getDefaultTicketsPerPurchase() {
        return defaultTicketsPerPurchase;
    }

    public void setDefaultTicketsPerPurchase(final int defaultTicketsPerPurchase) {
        this.defaultTicketsPerPurchase = defaultTicketsPerPurchase;
    }

    public int getDefaultPosCount() {
        return defaultPosCount;
    }

    public void setDefaultPosCount(final int defaultPosCount) {
        this.defaultPosCount = defaultPosCount;
    }

    public int getPosFailureThreshold() {
        return posFailureThreshold;
    }

    public void setPosFailureThreshold(final int posFailureThreshold) {
        this.posFailureThreshold = posFailureThreshold;
    }

    public int getHealthCheckIntervalMs() {
        return healthCheckIntervalMs;
    }

    public void setHealthCheckIntervalMs(final int healthCheckIntervalMs) {
        this.healthCheckIntervalMs = healthCheckIntervalMs;
    }

    public int getSseIntervalMs() {
        return sseIntervalMs;
    }

    public void setSseIntervalMs(final int sseIntervalMs) {
        this.sseIntervalMs = sseIntervalMs;
    }

    public int getPosReallocationRetryMs() {
        return posReallocationRetryMs;
    }

    public void setPosReallocationRetryMs(final int posReallocationRetryMs) {
        this.posReallocationRetryMs = posReallocationRetryMs;
    }

    public int getPosAllocationMaxRetry() {
        return posAllocationMaxRetry;
    }

    public void setPosAllocationMaxRetry(final int posAllocationMaxRetry) {
        this.posAllocationMaxRetry = posAllocationMaxRetry;
    }
}
