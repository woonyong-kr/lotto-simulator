package org.woonyong.lotto.bot.service;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;
import static org.woonyong.lotto.core.constant.ErrorMessageConstants.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.bot.config.BotClientConfig;

@Service
public class DashboardReportService {

  private static final String BOT_STATUS_ENDPOINT = "/api/dashboard/bot-status";
  private static final String POS_STATUS_ENDPOINT = "/api/dashboard/pos-status";

  private final WebClient webClient = WebClient.builder().build();
  private final String dashboardUrl;

  public DashboardReportService(final BotClientConfig botClientConfig) {
    this.dashboardUrl = botClientConfig.getDashboardServerUrl();
  }

  public void reportBotStatus(String botId, boolean isActive, String responseTime) {
    Map<String, Object> botStatus = new HashMap<>();
    botStatus.put(BOT_UID, botId);
    botStatus.put(IS_ACTIVE, isActive);
    botStatus.put(RESPONSE_TIME, responseTime);
    sendToDashboard(BOT_STATUS_ENDPOINT, botStatus);
  }

  public void reportPosStatus(String botId, Map<String, Object> posStatuses) {
    Map<String, Object> report = new HashMap<>();
    report.put(BOT_UID, botId);
    report.putAll(posStatuses);
    sendToDashboard(POS_STATUS_ENDPOINT, report);
  }

  private void sendToDashboard(String endpoint, Map<String, Object> data) {
    try {
      data.put(TIMESTAMP, System.currentTimeMillis());
      String url = dashboardUrl + endpoint;
      webClient.post().uri(url).bodyValue(data).retrieve().bodyToMono(String.class).subscribe();
    } catch (Exception e) {
      System.err.println(DASHBOARD_REPORT_FAILURE_MESSAGE + e.getMessage());
    }
  }
}
