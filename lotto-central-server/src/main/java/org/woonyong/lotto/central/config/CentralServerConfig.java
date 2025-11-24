package org.woonyong.lotto.central.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "central.server")
public class CentralServerConfig {
  private String dashboardUrl;
  private int defaultPosCount;
}
