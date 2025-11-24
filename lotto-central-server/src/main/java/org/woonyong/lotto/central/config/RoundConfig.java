package org.woonyong.lotto.central.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lotto.round")
public class RoundConfig {
    private int openDuration;
    private int closedDuration;
    private int checkInterval;
    private int sseInterval;

    public int getOpenDuration() {
        return openDuration;
    }

    public void setOpenDuration(final int openDuration) {
        this.openDuration = openDuration;
    }

    public int getClosedDuration() {
        return closedDuration;
    }

    public void setClosedDuration(final int closedDuration) {
        this.closedDuration = closedDuration;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(final int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public int getSseInterval() {
        return sseInterval;
    }

    public void setSseInterval(final int sseInterval) {
        this.sseInterval = sseInterval;
    }
}