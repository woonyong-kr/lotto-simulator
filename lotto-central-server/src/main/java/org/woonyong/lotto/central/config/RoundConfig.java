package org.woonyong.lotto.central.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.round")
public class RoundConfig {
    private final int openDuration;
    private final int closedDuration;
    private final int checkInterval;

    public RoundConfig(final int openDuration,
                       final int closedDuration,
                       final int checkInterval) {
        this.openDuration = openDuration;
        this.closedDuration = closedDuration;
        this.checkInterval = checkInterval;
    }
}