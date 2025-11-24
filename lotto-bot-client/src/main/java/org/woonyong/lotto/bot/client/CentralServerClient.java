package org.woonyong.lotto.bot.client;

import static org.woonyong.lotto.core.constant.JsonKeyConstants.ACTIVE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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

  private final RestTemplate restTemplate;
  private final String centralServerUrl;

  public CentralServerClient(
      final RestTemplate restTemplate, final BotClientConfig botClientConfig) {
    this.restTemplate = restTemplate;
    this.centralServerUrl = botClientConfig.getCentralServerUrl();
  }

  public List<BotDataResponse> getActiveBots() {
    String url = centralServerUrl + BOTS_ENDPOINT + ACTIVE_BOTS_QUERY;
    try {
      ResponseEntity<ApiResponse<List<BotDataResponse>>> response =
          restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
      return extractData(response);
    } catch (Exception e) {
      log.error("활성 봇 조회 실패: {}", e.getMessage());
      return Collections.emptyList();
    }
  }

  public BotDataResponse createBot() {
    String url = centralServerUrl + BOTS_ENDPOINT;
    try {
      ResponseEntity<ApiResponse<BotDataResponse>> response =
          restTemplate.exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<>() {});
      return extractSingleData(response);
    } catch (Exception e) {
      log.error("봇 생성 실패: {}", e.getMessage());
      return null;
    }
  }

  public boolean deactivateBot(final String botUid) {
    String url = centralServerUrl + BOTS_ENDPOINT + "/" + botUid + DEACTIVATE_SUFFIX;
    try {
      restTemplate.put(url, null);
      return true;
    } catch (Exception e) {
      log.error("봇 비활성화 실패: {} - {}", botUid, e.getMessage());
      return false;
    }
  }

  public boolean deactivatePos(final String posUid) {
    String url = centralServerUrl + POS_ENDPOINT + posUid + STATUS_SUFFIX;
    try {
      HttpEntity<Map<String, Boolean>> entity = createDeactivateRequest();
      restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
      return true;
    } catch (Exception e) {
      log.error("POS 비활성화 실패: {} - {}", posUid, e.getMessage());
      return false;
    }
  }

  private HttpEntity<Map<String, Boolean>> createDeactivateRequest() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(Map.of(ACTIVE, false), headers);
  }

  private <T> T extractSingleData(final ResponseEntity<ApiResponse<T>> response) {
    if (response.getBody() == null) {
      return null;
    }
    return response.getBody().getData();
  }

  private <T> List<T> extractData(final ResponseEntity<ApiResponse<List<T>>> response) {
    if (response.getBody() == null || response.getBody().getData() == null) {
      return Collections.emptyList();
    }
    return response.getBody().getData();
  }
}
