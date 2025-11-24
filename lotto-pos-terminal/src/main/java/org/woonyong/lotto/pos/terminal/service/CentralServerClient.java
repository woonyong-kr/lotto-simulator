package org.woonyong.lotto.pos.terminal.service;

import java.time.Duration;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;
import org.woonyong.lotto.pos.terminal.exception.PosTerminalException;

@Component
public class CentralServerClient {
  private static final String TICKETS_ENDPOINT = "/api/tickets";
  private static final String CURRENT_ROUND_ENDPOINT = "/api/rounds/current";
  private static final String STATISTICS_ENDPOINT = "/api/pos-terminals/statistics";
  private static final String AUTO_TYPE = "AUTO";

  private static final String ERROR_CODE_CURRENT_ROUND_NOT_FOUND = "CURRENT_ROUND_NOT_FOUND";
  private static final String ERROR_CODE_TICKET_PURCHASE_FAILED = "TICKET_PURCHASE_FAILED";
  private static final String ERROR_CODE_STATISTICS_REPORT_FAILED = "STATISTICS_REPORT_FAILED";

  private static final String ERROR_CURRENT_ROUND_NOT_FOUND = "현재 회차 정보를 가져올 수 없습니다";
  private static final String ERROR_TICKET_PURCHASE_FAILED = "티켓 구매에 실패했습니다";
  private static final String ERROR_STATISTICS_REPORT_FAILED = "통계 전송에 실패했습니다";

  private final WebClient webClient;
  private final PosTerminalConfig config;

  public CentralServerClient(final WebClient webClient, final PosTerminalConfig config) {
    this.webClient = webClient;
    this.config = config;
  }

  public String purchaseAutoTicket(final int count, final String posUid) {
    Long currentRoundId = getCurrentRoundId();

    Map<String, Object> requestBody =
        Map.of(
            "roundId", currentRoundId,
            "type", AUTO_TYPE,
            "count", count,
            "posUid", posUid);

    ApiResponse response =
        webClient
            .post()
            .uri(config.getCentralServerUrl() + TICKETS_ENDPOINT)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(ApiResponse.class)
            .timeout(Duration.ofMillis(config.getReadTimeout()))
            .block();

    return extractTicketNumber(response);
  }

  private Long getCurrentRoundId() {
    ApiResponse response =
        webClient
            .get()
            .uri(config.getCentralServerUrl() + CURRENT_ROUND_ENDPOINT)
            .retrieve()
            .bodyToMono(ApiResponse.class)
            .timeout(Duration.ofMillis(config.getReadTimeout()))
            .block();

    if (response == null) {
      throw PosTerminalException.of(
          ERROR_CODE_CURRENT_ROUND_NOT_FOUND, ERROR_CURRENT_ROUND_NOT_FOUND);
    }

    Map<String, Object> data = (Map<String, Object>) response.getData();
    return ((Number) data.get("id")).longValue();
  }

  public void reportStatistics(final String terminalId, final Long averageResponseTime) {
    Map<String, Object> requestBody =
        Map.of(
            "terminalId", terminalId,
            "averageResponseTime", averageResponseTime);

    try {
      webClient
          .post()
          .uri(config.getCentralServerUrl() + STATISTICS_ENDPOINT)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(ApiResponse.class)
          .timeout(Duration.ofMillis(config.getReadTimeout()))
          .block();
    } catch (Exception e) {
      throw PosTerminalException.of(
          ERROR_CODE_STATISTICS_REPORT_FAILED, ERROR_STATISTICS_REPORT_FAILED, e);
    }
  }

  private String extractTicketNumber(final ApiResponse response) {
    if (response == null) {
      throw PosTerminalException.of(
          ERROR_CODE_TICKET_PURCHASE_FAILED, ERROR_TICKET_PURCHASE_FAILED);
    }

    Map<String, Object> data = (Map<String, Object>) response.getData();
    return (String) data.get("ticketNumber");
  }
}
