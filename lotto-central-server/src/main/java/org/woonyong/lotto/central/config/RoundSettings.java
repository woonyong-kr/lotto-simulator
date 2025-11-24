package org.woonyong.lotto.central.config;

import org.springframework.stereotype.Component;

@Component
public class RoundSettings {
    private int openDuration;
    private int closedDuration;

    public RoundSettings(final RoundConfig roundConfig) {
        this.openDuration = roundConfig.getOpenDuration();
        this.closedDuration = roundConfig.getClosedDuration();
    }

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
}