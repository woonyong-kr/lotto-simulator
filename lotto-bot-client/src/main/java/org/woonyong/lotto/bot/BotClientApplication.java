package org.woonyong.lotto.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotClientApplication.class, args);
    }
}