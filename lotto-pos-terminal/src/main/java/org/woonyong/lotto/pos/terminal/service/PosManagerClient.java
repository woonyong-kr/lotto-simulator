package org.woonyong.lotto.pos.terminal.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;
import org.woonyong.lotto.pos.terminal.exception.PosTerminalException;

import java.time.Duration;
import java.util.Map;

@Component
public class PosManagerClient {
    private static final String REGISTER_TERMINAL_ENDPOINT = "/api/terminals/register";
    private static final String ERROR_CODE_POS_MANAGER_UNREACHABLE = "POS_MANAGER_UNREACHABLE";
    private static final String ERROR_POS_MANAGER_UNREACHABLE = "POS 매니저에 연결할 수 없습니다";

    private final WebClient webClient;
    private final PosTerminalConfig config;

    public PosManagerClient(final WebClient webClient, final PosTerminalConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    public void registerTerminal(final String terminalId) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "terminalId", terminalId
            );

            webClient.post()
                    .uri(config.getPosManagerUrl() + REGISTER_TERMINAL_ENDPOINT)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(config.getReadTimeout()))
                    .block();

        } catch (Exception e) {
            throw PosTerminalException.of(ERROR_CODE_POS_MANAGER_UNREACHABLE, ERROR_POS_MANAGER_UNREACHABLE, e);
        }
    }
}