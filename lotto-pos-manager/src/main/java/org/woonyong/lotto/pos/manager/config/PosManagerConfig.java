package org.woonyong.lotto.pos.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pos.manager")
public class PosManagerConfig {
  private int maxPosCapacity;
  private int healthCheckIntervalMs;
  private int sseIntervalMs;
  private String dashboardUrl;
}
