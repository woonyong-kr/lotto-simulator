package org.woonyong.lotto.central.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lotto.round")
public class RoundConfig {
    private int openDuration;
    private int closedDuration;
    private int checkInterval;

    public void setOpenDuration(int openDuration) {
        this.openDuration = openDuration;
    }

    public void setClosedDuration(int closedDuration) {
        this.closedDuration = closedDuration;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public int getOpenDuration() {
        return openDuration;
    }

    public int getClosedDuration() {
        return closedDuration;
    }

    public int getCheckInterval() {
        return checkInterval;
    }
}