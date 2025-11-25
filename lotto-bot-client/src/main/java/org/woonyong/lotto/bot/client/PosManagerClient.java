package org.woonyong.lotto.bot.client;

import java.time.Duration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.dto.request.AllocateTerminalRequest;
import org.woonyong.lotto.bot.dto.request.ReleaseTerminalRequest;
import org.woonyong.lotto.bot.dto.response.AllocateTerminalResponse;
import org.woonyong.lotto.core.dto.ApiResponse;

@Component
public class PosManagerClient {
  private static final String TERMINALS_ALLOCATE_ENDPOINT = "/api/terminals/allocate";
  private static final String TERMINALS_RELEASE_ENDPOINT = "/api/terminals/release";

  private final WebClient webClient;
  private final String posManagerUrl;
  private final String botClientUrl;
  private final Duration readTimeout;

  public PosManagerClient(final WebClient webClient, final BotClientConfig botClientConfig) {
    this.webClient = webClient;
    this.posManagerUrl = botClientConfig.getPosManagerUrl();
    this.botClientUrl = botClientConfig.getBotClientUrl();
    this.readTimeout = Duration.ofMillis(botClientConfig.getReadTimeout());
  }

  public AllocateTerminalResponse allocateTerminal(final String botUid, final String posUid) {
    String url = posManagerUrl + TERMINALS_ALLOCATE_ENDPOINT;
    AllocateTerminalRequest request = new AllocateTerminalRequest(botUid, posUid, botClientUrl);
    try {
      ApiResponse<AllocateTerminalResponse> response = webClient
          .post()
          .uri(url)
          .bodyValue(request)
          .retrieve()
          .bodyToMono(ApiResponse.class)
          .timeout(readTimeout)
          .block();

      if (response != null && response.isSuccess()) {
        return response.getData();
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public boolean releaseTerminal(final String terminalId, final String botUid, final String posUid) {
    String url = posManagerUrl + TERMINALS_RELEASE_ENDPOINT;
    ReleaseTerminalRequest request = new ReleaseTerminalRequest(terminalId, botUid, posUid);
    try {
      webClient
          .post()
          .uri(url)
          .bodyValue(request)
          .retrieve()
          .bodyToMono(String.class)
          .timeout(readTimeout)
          .block();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
