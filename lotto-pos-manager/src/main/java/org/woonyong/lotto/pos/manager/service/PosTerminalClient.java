package org.woonyong.lotto.pos.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.manager.config.PosManagerConfig;
import org.woonyong.lotto.pos.manager.exception.PosManagerException;

import java.time.Duration;
import java.util.Map;

@Component
public class PosTerminalClient {
    private static final String OWNER_ENDPOINT = "/api/owner";
    private static final String HEALTH_ENDPOINT = "/health";

    private static final String ERROR_CODE_OWNER_ASSIGN_FAILED = "OWNER_ASSIGN_FAILED";
    private static final String ERROR_CODE_HEALTH_CHECK_FAILED = "HEALTH_CHECK_FAILED";

    private static final String ERROR_OWNER_ASSIGN_FAILED = "오너 할당에 실패했습니다";
    private static final String ERROR_HEALTH_CHECK_FAILED = "헬스체크에 실패했습니다";

    private final WebClient webClient;
    private final PosManagerConfig config;

    public PosTerminalClient(final WebClient webClient, final PosManagerConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    public void assignOwner(final String terminalAddress, final String botUid, final String posUid, final String botClientUrl) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "botUid", botUid,
                    "posUid", posUid,
                    "botClientUrl", botClientUrl
            );

            webClient.post()
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
            Map<String, Object> response = webClient.get()
                    .uri(terminalAddress + HEALTH_ENDPOINT)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(5000))
                    .block();

            return response != null && Boolean.TRUE.equals(response.get("success"));

        } catch (Exception e) {
            return false;
        }
    }
}