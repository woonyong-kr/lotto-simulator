package org.woonyong.lotto.pos.manager.service;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;
import static org.woonyong.lotto.core.constant.ErrorMessageConstants.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.pos.manager.config.PosManagerConfig;

@Service
public class DashboardReportService {

  private static final String SYSTEM_METRICS_ENDPOINT = "/api/dashboard/system-metrics";
  private static final String POS_TERMINAL_STATUS_ENDPOINT = "/api/dashboard/pos-terminal-status";
  private static final String REPORT_SOURCE = "pos-manager";

  private final WebClient webClient = WebClient.builder().build();
  private final String dashboardUrl;

  public DashboardReportService(final PosManagerConfig posManagerConfig) {
    this.dashboardUrl = posManagerConfig.getDashboardUrl();
  }

  public void reportPosSystemMetrics(Map<String, Object> metrics) {
    Map<String, Object> report = new HashMap<>();
    report.put(SOURCE, REPORT_SOURCE);
    report.put(METRICS, metrics);
    sendToDashboard(SYSTEM_METRICS_ENDPOINT, report);
  }

  public void reportPosTerminalStatus(
      String terminalId, boolean isActive, Map<String, Object> status) {
    Map<String, Object> report = new HashMap<>();
    report.put(TERMINAL_ID, terminalId);
    report.put(IS_ACTIVE, isActive);
    report.put(STATUS, status);
    sendToDashboard(POS_TERMINAL_STATUS_ENDPOINT, report);
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
