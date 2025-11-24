package org.woonyong.lotto.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotClientConfig {
  private int maxCapacity;
  private int defaultPurchaseIntervalMs;
  private int defaultTicketsPerPurchase;
  private int defaultPosCount;
  private int posFailureThreshold;
  private int healthCheckIntervalMs;
  private int sseIntervalMs;
  private int posReallocationRetryMs;
  private int posAllocationMaxRetry;
  private int dashboardReportIntervalMs;

  private String centralServerUrl;
  private String posManagerUrl;
  private String dashboardServerUrl;
}
