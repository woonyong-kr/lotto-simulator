package org.woonyong.lotto.pos.terminal.service;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;
import static org.woonyong.lotto.core.constant.ErrorMessageConstants.*;

import java.time.Duration;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;
import org.woonyong.lotto.pos.terminal.exception.PosTerminalException;

@Component
public class OwnerValidationClient {
  private static final String HEALTH_OWNER_ENDPOINT = "/api/health/owner";
  private static final String ERROR_CODE_BOT_CLIENT_UNREACHABLE = "BOT_CLIENT_UNREACHABLE";

  private final WebClient webClient;
  private final PosTerminalConfig config;

  public OwnerValidationClient(final WebClient webClient, final PosTerminalConfig config) {
    this.webClient = webClient;
    this.config = config;
  }

  public boolean checkOwnerValidity(
      final String botClientUrl,
      final String ownerBotUid,
      final String posUid,
      final String terminalId) {
    try {
      Map<String, Object> requestBody =
          Map.of(
              OWNER_BOT_UID, ownerBotUid,
              POS_UID, posUid,
              TERMINAL_ID, terminalId);

      Map<String, Object> apiResponse =
          webClient
              .post()
              .uri(botClientUrl + HEALTH_OWNER_ENDPOINT)
              .bodyValue(requestBody)
              .retrieve()
              .bodyToMono(Map.class)
              .timeout(Duration.ofMillis(config.getReadTimeout()))
              .block();

      if (apiResponse == null || !Boolean.TRUE.equals(apiResponse.get(SUCCESS))) {
        return false;
      }

      Map<String, Object> data = (Map<String, Object>) apiResponse.get(DATA);
      return data != null && Boolean.TRUE.equals(data.get(VALID));

    } catch (Exception e) {
      throw PosTerminalException.of(
          ERROR_CODE_BOT_CLIENT_UNREACHABLE, ERROR_BOT_CLIENT_UNREACHABLE, e);
    }
  }
}
