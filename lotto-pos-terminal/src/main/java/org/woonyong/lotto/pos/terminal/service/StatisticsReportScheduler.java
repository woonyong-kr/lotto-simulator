package org.woonyong.lotto.pos.terminal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;

@Component
public class StatisticsReportScheduler {
    private static final String LOG_STATISTICS_REPORT = "통계 전송: 터미널 ID={}, 평균 응답시간={}ms";
    private static final String LOG_NO_STATISTICS = "전송할 통계 데이터가 없습니다";
    private static final String ERROR_STATISTICS_SEND_FAILED = "통계 전송 실패: ";

    private final ResponseTimeCollector responseTimeCollector;
    private final TerminalInitializer terminalInitializer;
    private final CentralServerClient centralServerClient;
    private final PosTerminalConfig config;

    public StatisticsReportScheduler(final ResponseTimeCollector responseTimeCollector,
                                   final TerminalInitializer terminalInitializer,
                                   final CentralServerClient centralServerClient,
                                   final PosTerminalConfig config) {
        this.responseTimeCollector = responseTimeCollector;
        this.terminalInitializer = terminalInitializer;
        this.centralServerClient = centralServerClient;
        this.config = config;
    }

    @Scheduled(fixedRateString = "#{posTerminalConfig.statisticsReportInterval}")
    public void reportStatistics() {
        Long averageResponseTime = responseTimeCollector.calculateAverageAndClear();

        if (averageResponseTime == null) {
            System.out.println(LOG_NO_STATISTICS);
            return;
        }

        String terminalId = terminalInitializer.getTerminalId();

        try {
            centralServerClient.reportStatistics(terminalId, averageResponseTime);
            System.out.printf(LOG_STATISTICS_REPORT + "%n", terminalId, averageResponseTime);
        } catch (Exception e) {
            System.err.println(ERROR_STATISTICS_SEND_FAILED + e.getMessage());
        }
    }
}