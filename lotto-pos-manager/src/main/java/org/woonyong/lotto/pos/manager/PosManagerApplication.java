package org.woonyong.lotto.pos.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PosManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(PosManagerApplication.class, args);
  }
}
