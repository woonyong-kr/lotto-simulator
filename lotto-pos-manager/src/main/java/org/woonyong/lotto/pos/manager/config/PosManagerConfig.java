package org.woonyong.lotto.pos.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pos.manager")
public class PosManagerConfig {
    private int maxPosCapacity;
    private int healthCheckIntervalMs;
    private int sseIntervalMs;

    public int getMaxPosCapacity() {
        return maxPosCapacity;
    }

    public void setMaxPosCapacity(final int maxPosCapacity) {
        this.maxPosCapacity = maxPosCapacity;
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
}