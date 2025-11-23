package org.woonyong.lotto.central.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.central.service.RoundService;

@Component
public class RoundInitializer implements ApplicationRunner {
    private final RoundService roundService;

    public RoundInitializer(final RoundService roundService) {
        this.roundService = roundService;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (roundService.getCurrentRound().isEmpty()) {
            roundService.startFirstRound();
        }
    }
}