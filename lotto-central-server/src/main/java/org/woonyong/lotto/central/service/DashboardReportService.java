package org.woonyong.lotto.central.service;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;
import static org.woonyong.lotto.core.constant.ErrorMessageConstants.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.central.config.CentralServerConfig;

@Service
public class DashboardReportService {

  private static final String ROUND_UPDATE_ENDPOINT = "/api/dashboard/round-update";
  private static final String SYSTEM_METRICS_ENDPOINT = "/api/dashboard/system-metrics";
  private static final String TICKET_SALES_ENDPOINT = "/api/dashboard/ticket-sales";
  private static final String REPORT_SOURCE = "central-server";

  private final WebClient webClient = WebClient.builder().build();
  private final String dashboardUrl;

  public DashboardReportService(final CentralServerConfig centralServerConfig) {
    this.dashboardUrl = centralServerConfig.getDashboardUrl();
  }

  public void reportRoundUpdate(Map<String, Object> roundData) {
    Map<String, Object> report = new HashMap<>();
    report.put(SOURCE, REPORT_SOURCE);
    report.put(ROUND_DATA, roundData);
    sendToDashboard(ROUND_UPDATE_ENDPOINT, report);
  }

  public void reportSystemMetrics(Map<String, Object> metrics) {
    Map<String, Object> report = new HashMap<>();
    report.put(SOURCE, REPORT_SOURCE);
    report.put(METRICS, metrics);
    sendToDashboard(SYSTEM_METRICS_ENDPOINT, report);
  }

  public void reportTicketSales(Map<String, Object> salesData) {
    Map<String, Object> report = new HashMap<>();
    report.put(SOURCE, REPORT_SOURCE);
    report.put(SALES_DATA, salesData);
    sendToDashboard(TICKET_SALES_ENDPOINT, report);
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
