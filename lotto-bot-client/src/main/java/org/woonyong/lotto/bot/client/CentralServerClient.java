package org.woonyong.lotto.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.woonyong.lotto.bot.dto.response.BotDataResponse;
import org.woonyong.lotto.core.dto.ApiResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class CentralServerClient {
    private static final Logger log = LoggerFactory.getLogger(CentralServerClient.class);

    private final RestTemplate restTemplate;
    private final String centralServerUrl;

    public CentralServerClient(
            final RestTemplate restTemplate,
            @Value("${central.server.url}") final String centralServerUrl
    ) {
        this.restTemplate = restTemplate;
        this.centralServerUrl = centralServerUrl;
    }

    public List<BotDataResponse> getActiveBots() {
        String url = centralServerUrl + "/api/bots?active=true";
        try {
            ResponseEntity<ApiResponse<List<BotDataResponse>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return extractData(response);
        } catch (Exception e) {
            log.error("활성 봇 조회 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public BotDataResponse createBot() {
        String url = centralServerUrl + "/api/bots";
        try {
            ResponseEntity<ApiResponse<BotDataResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return extractSingleData(response);
        } catch (Exception e) {
            log.error("봇 생성 실패: {}", e.getMessage());
            return null;
        }
    }

    public boolean deactivateBot(final String botUid) {
        String url = centralServerUrl + "/api/bots/" + botUid + "/deactivate";
        try {
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            log.error("봇 비활성화 실패: {} - {}", botUid, e.getMessage());
            return false;
        }
    }

    public boolean deactivatePos(final String posUid) {
        String url = centralServerUrl + "/api/pos/" + posUid + "/status";
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
        return new HttpEntity<>(Map.of("active", false), headers);
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
