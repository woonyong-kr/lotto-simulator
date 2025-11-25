package org.woonyong.lotto.bot.client;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.*;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.dto.response.BotDataResponse;
import org.woonyong.lotto.core.dto.ApiResponse;

@Component
public class CentralServerClient {
  private static final Logger log = LoggerFactory.getLogger(CentralServerClient.class);
  private static final String BOTS_ENDPOINT = "/api/bots";
  private static final String ACTIVE_BOTS_QUERY = "?active=true";
  private static final String DEACTIVATE_SUFFIX = "/deactivate";
  private static final String POS_ENDPOINT = "/api/pos/";
  private static final String STATUS_SUFFIX = "/status";

  private final WebClient webClient;
  private final String centralServerUrl;
  private final Duration readTimeout;

  public CentralServerClient(
      final WebClient webClient, final BotClientConfig botClientConfig) {
    this.webClient = webClient;
    this.centralServerUrl = botClientConfig.getCentralServerUrl();
    this.readTimeout = Duration.ofMillis(botClientConfig.getReadTimeout());
  }

  public List<BotDataResponse> getActiveBots() {
    String url = centralServerUrl + BOTS_ENDPOINT + ACTIVE_BOTS_QUERY;
    try {
      ApiResponse<List<BotDataResponse>> response = webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToMono(ApiResponse.class)
          .timeout(readTimeout)
          .block();
      return extractData(response);
    } catch (Exception e) {
      log.error("활성 봇 조회 실패: {}", e.getMessage());
      return Collections.emptyList();
    }
  }

  public BotDataResponse createBot() {
    String url = centralServerUrl + BOTS_ENDPOINT;
    try {
      ApiResponse<BotDataResponse> response = webClient
          .post()
          .uri(url)
          .retrieve()
          .bodyToMono(ApiResponse.class)
          .timeout(readTimeout)
          .block();
      return extractSingleData(response);
    } catch (Exception e) {
      log.error("봇 생성 실패: {}", e.getMessage());
      return null;
    }
  }

  public boolean deactivateBot(final String botUid) {
    String url = centralServerUrl + BOTS_ENDPOINT + "/" + botUid + DEACTIVATE_SUFFIX;
    try {
      webClient
          .put()
          .uri(url)
          .retrieve()
          .bodyToMono(Void.class)
          .timeout(readTimeout)
          .block();
      return true;
    } catch (Exception e) {
      log.error("봇 비활성화 실패: {} - {}", botUid, e.getMessage());
      return false;
    }
  }

  public boolean deactivatePos(final String posUid) {
    String url = centralServerUrl + POS_ENDPOINT + posUid + STATUS_SUFFIX;
    try {
      webClient
          .put()
          .uri(url)
          .bodyValue(Map.of(ACTIVE, false))
          .retrieve()
          .bodyToMono(Void.class)
          .timeout(readTimeout)
          .block();
      return true;
    } catch (Exception e) {
      log.error("POS 비활성화 실패: {} - {}", posUid, e.getMessage());
      return false;
    }
  }

  private <T> T extractSingleData(final ApiResponse<T> response) {
    if (response == null) {
      return null;
    }
    return response.getData();
  }

  private <T> List<T> extractData(final ApiResponse<List<T>> response) {
    if (response == null || response.getData() == null) {
      return Collections.emptyList();
    }
    return response.getData();
  }
}
