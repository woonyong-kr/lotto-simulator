package org.woonyong.lotto.central.service;

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

    public void reportRoundUpdate(Map<String, Object> roundData) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("source", "central-server");
            report.put("roundData", roundData);
            report.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/round-update";
            webClient.post()
                    .uri(url)
                    .bodyValue(report)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("대시보드 회차 업데이트 리포트 실패: " + e.getMessage());
        }
    }

    public void reportSystemMetrics(Map<String, Object> metrics) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("source", "central-server");
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
            System.err.println("대시보드 중앙 서버 시스템 메트릭 리포트 실패: " + e.getMessage());
        }
    }

    public void reportTicketSales(Map<String, Object> salesData) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("source", "central-server");
            report.put("salesData", salesData);
            report.put("timestamp", System.currentTimeMillis());

            String url = dashboardUrl + "/api/dashboard/ticket-sales";
            webClient.post()
                    .uri(url)
                    .bodyValue(report)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe();
        } catch (Exception e) {
            System.err.println("대시보드 티켓 판매 데이터 리포트 실패: " + e.getMessage());
        }
    }
}