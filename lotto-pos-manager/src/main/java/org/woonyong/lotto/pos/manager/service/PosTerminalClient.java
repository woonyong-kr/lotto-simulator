package org.woonyong.lotto.pos.manager.service;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;
import static org.woonyong.lotto.core.constant.ErrorMessageConstants.*;

import java.time.Duration;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.manager.config.PosManagerConfig;
import org.woonyong.lotto.pos.manager.exception.PosManagerException;

@Component
public class PosTerminalClient {
  private static final String OWNER_ENDPOINT = "/api/owner";
  private static final String HEALTH_ENDPOINT = "/health";

  private static final String ERROR_CODE_OWNER_ASSIGN_FAILED = "OWNER_ASSIGN_FAILED";
  private final WebClient webClient;
  private final PosManagerConfig config;

  public PosTerminalClient(final WebClient webClient, final PosManagerConfig config) {
    this.webClient = webClient;
    this.config = config;
  }

  public void assignOwner(
      final String terminalAddress,
      final String botUid,
      final String posUid,
      final String botClientUrl) {
    try {
      Map<String, Object> requestBody =
          Map.of(
              BOT_UID, botUid,
              POS_UID, posUid,
              BOT_CLIENT_URL, botClientUrl);

      webClient
          .post()
          .uri(terminalAddress + OWNER_ENDPOINT)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(ApiResponse.class)
          .timeout(Duration.ofMillis(5000))
          .block();

    } catch (Exception e) {
      throw PosManagerException.of(ERROR_CODE_OWNER_ASSIGN_FAILED, ERROR_OWNER_ASSIGN_FAILED, e);
    }
  }

  public boolean checkHealth(final String terminalAddress) {
    try {
      Map<String, Object> response =
          webClient
              .get()
              .uri(terminalAddress + HEALTH_ENDPOINT)
              .retrieve()
              .bodyToMono(Map.class)
              .timeout(Duration.ofMillis(5000))
              .block();

      return response != null && Boolean.TRUE.equals(response.get(SUCCESS));

    } catch (Exception e) {
      return false;
    }
  }
}
