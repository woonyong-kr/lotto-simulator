package org.woonyong.lotto.bot.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.bot.client.CentralServerClient;
import org.woonyong.lotto.bot.dto.response.BotDataResponse;
import org.woonyong.lotto.bot.service.BotService;

@Component
public class BotClientInitializer {

  private final CentralServerClient centralServerClient;
  private final BotService botService;
  private final BotClientConfig config;

  public BotClientInitializer(
      final CentralServerClient centralServerClient,
      final BotService botService,
      final BotClientConfig config) {
    this.centralServerClient = centralServerClient;
    this.botService = botService;
    this.config = config;
  }

  @PostConstruct
  public void initialize() {
    List<BotDataResponse> activeBots = centralServerClient.getActiveBots();
    initializeBotsWithCapacityCheck(activeBots);
  }

  private void initializeBotsWithCapacityCheck(final List<BotDataResponse> activeBots) {
    int maxCapacity = config.getMaxCapacity();
    int count = 0;
    for (BotDataResponse botData : activeBots) {
      if (count >= maxCapacity) {
        deactivateExcessBot(botData);
        continue;
      }
      botService.initializeBotInstance(botData);
      count++;
    }
  }

  private void deactivateExcessBot(final BotDataResponse botData) {
    centralServerClient.deactivateBot(botData.getBotUid());
  }
}
