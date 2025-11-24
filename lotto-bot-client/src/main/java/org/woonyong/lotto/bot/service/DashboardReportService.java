package org.woonyong.lotto.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardReportService {

    @Value("${dashboard.server.url:http://localhost:8500}")
    private String dashboardUrl;

    private final WebClient webClient = WebClient.builder().build();

    public void reportBotStatus(String botId, boolean isActive, String responseTime) {
        try {
            Map<String, Object> botStatus = new HashMap<>();
            botStatus.put("botId", botId);
            botStatus.put("isActive", isActive);
            botStatus.put("responseTime", responseTime);
            botStatus.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/bot-status";
            webClient.post()
                    .uri(url)
                    .bodyValue(botStatus)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            // 대시보드 연결 실패는 조용히 무시 (로그만 출력)
            System.err.println("대시보드 상태 리포트 실패: " + e.getMessage());
        }
    }

    public void reportPosStatus(String botId, Map<String, Object> posStatuses) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("botId", botId);
            // posStatuses 안에 이미 "positions" 키가 있으므로 그대로 넣음
            report.putAll(posStatuses);
            report.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/pos-status";
            webClient.post()
                    .uri(url)
                    .bodyValue(report)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("대시보드 POS 상태 리포트 실패: " + e.getMessage());
        }
    }
}