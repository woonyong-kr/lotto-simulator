package org.woonyong.lotto.central.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.woonyong.lotto.central.service.RoundService;

@Configuration
public class RoundInitializer {

    @Bean
    public CommandLineRunner initializeRound(final RoundService roundService) {
        return args -> {
            if (roundService.getCurrentRound().isEmpty()) {
                roundService.startFirstRound();
            }
        };
    }
}