package org.woonyong.lotto.pos.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PosTerminalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PosTerminalApplication.class, args);
    }
}
