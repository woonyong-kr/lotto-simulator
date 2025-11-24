package org.woonyong.lotto.pos.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardReportService {

    @Value("${dashboard.server.url:http://localhost:8580}")
    private String dashboardUrl;

    private final WebClient webClient = WebClient.builder().build();

    public void reportPosSystemMetrics(Map<String, Object> metrics) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("source", "pos-manager");
            report.put("metrics", metrics);
            report.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/system-metrics";
            webClient.post()
                    .uri(url)
                    .bodyValue(report)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("대시보드 POS 시스템 메트릭 리포트 실패: " + e.getMessage());
        }
    }

    public void reportPosTerminalStatus(String terminalId, boolean isActive, Map<String, Object> status) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("terminalId", terminalId);
            report.put("isActive", isActive);
            report.put("status", status);
            report.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/pos-terminal-status";
            webClient.post()
                    .uri(url)
                    .bodyValue(report)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("대시보드 POS 터미널 상태 리포트 실패: " + e.getMessage());
        }
    }
}