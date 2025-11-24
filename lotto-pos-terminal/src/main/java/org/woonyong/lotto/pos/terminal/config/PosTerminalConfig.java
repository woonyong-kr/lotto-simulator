package org.woonyong.lotto.pos.terminal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pos.terminal")
public class PosTerminalConfig {
  private String centralServerUrl;
  private String posManagerUrl;
  private int statisticsReportInterval;
  private int connectionTimeout;
  private int readTimeout;
  private int healthCheckIntervalMs;
  private int ownerValidationFailureThreshold;
}
