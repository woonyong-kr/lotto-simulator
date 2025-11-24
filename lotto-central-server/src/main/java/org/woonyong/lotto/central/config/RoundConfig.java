package org.woonyong.lotto.central.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "lotto.round")
public class RoundConfig {
  private int openDuration;
  private int closedDuration;
  private int checkInterval;
  private int sseInterval;
}
