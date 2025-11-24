package org.woonyong.lotto.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("org.woonyong.lotto.central.repository")
@EntityScan("org.woonyong.lotto.central.entity")
public class CentralServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CentralServerApplication.class, args);
    }
}