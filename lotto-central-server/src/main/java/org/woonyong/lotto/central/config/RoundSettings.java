package org.woonyong.lotto.central.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoundSettings {
    private int openDuration;
    private int closedDuration;

    public RoundSettings(
            @Value("#{@roundConfig.openDuration}") final int openDuration,
            @Value("#{@roundConfig.checkInterval}") final int closedDuration
    ) {
        this.openDuration = openDuration;
        this.closedDuration = closedDuration;
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