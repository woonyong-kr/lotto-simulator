package org.woonyong.lotto.pos.terminal.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;
import org.woonyong.lotto.pos.terminal.exception.PosTerminalException;

import java.time.Duration;
import java.util.Map;

@Component
public class OwnerValidationClient {
    private static final String HEALTH_OWNER_ENDPOINT = "/api/health/owner";
    private static final String ERROR_CODE_BOT_CLIENT_UNREACHABLE = "BOT_CLIENT_UNREACHABLE";
    private static final String ERROR_BOT_CLIENT_UNREACHABLE = "봇 클라이언트에 연결할 수 없습니다";

    private final WebClient webClient;
    private final PosTerminalConfig config;

    public OwnerValidationClient(final WebClient webClient, final PosTerminalConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    public boolean checkOwnerValidity(final String botClientUrl, final String ownerBotUid, final String posUid, final String terminalId) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "ownerBotUid", ownerBotUid,
                    "posUid", posUid,
                    "terminalId", terminalId
            );

            Map<String, Object> response = webClient.post()
                    .uri(botClientUrl + HEALTH_OWNER_ENDPOINT)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .block();

            return response != null && Boolean.TRUE.equals(response.get("valid"));

        } catch (Exception e) {
            throw PosTerminalException.of(ERROR_CODE_BOT_CLIENT_UNREACHABLE, ERROR_BOT_CLIENT_UNREACHABLE, e);
        }
    }
}