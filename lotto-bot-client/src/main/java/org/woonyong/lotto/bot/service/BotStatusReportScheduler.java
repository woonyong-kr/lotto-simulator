package org.woonyong.lotto.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.domain.BotInstance;
import org.woonyong.lotto.bot.domain.HealthStatus;
import org.woonyong.lotto.bot.domain.PosTerminalConnection;
import org.woonyong.lotto.bot.manager.BotInstanceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotStatusReportScheduler {
    private static final Logger log = LoggerFactory.getLogger(BotStatusReportScheduler.class);

    private final BotInstanceManager botInstanceManager;
    private final BotClientConfig config;
    private final DashboardReportService dashboardReportService;

    public BotStatusReportScheduler(
            final BotInstanceManager botInstanceManager,
            final BotClientConfig config,
            final DashboardReportService dashboardReportService
    ) {
        this.botInstanceManager = botInstanceManager;
        this.config = config;
        this.dashboardReportService = dashboardReportService;
    }

    @Scheduled(fixedRateString = "${bot.dashboard-report-interval-ms:2000}")
    public void reportBotStatus() {
        List<BotInstance> instances = botInstanceManager.getAllInstances();
        if (instances.isEmpty()) {
            return;
        }
        reportAllBotStatuses(instances);
    }

    private void reportAllBotStatuses(final List<BotInstance> instances) {
        for (BotInstance instance : instances) {
            reportSingleBotStatus(instance);
        }
    }

    private void reportSingleBotStatus(final BotInstance instance) {
        String botUid = instance.getBotUid();
        boolean isActive = instance.isRunning();
        String responseTime = calculateResponseTimeString(instance);

        dashboardReportService.reportBotStatus(botUid, isActive, responseTime);

        Map<String, Object> posData = createPosStatusData(instance);
        dashboardReportService.reportPosStatus(botUid, posData);

        logBotStatus(botUid, isActive, responseTime);
    }

    private String calculateResponseTimeString(final BotInstance instance) {
        instance.collectResponseTimesFromPos();
        Long avgTime = instance.calculateAverageResponseTime();
        instance.clearAggregatedResponseTimes();

        if (avgTime == null) {
            return "N/A";
        }
        return avgTime + "ms";
    }

    private Map<String, Object> createPosStatusData(final BotInstance instance) {
        List<Map<String, Object>> positions = createPosStatusList(instance);
        Map<String, Object> posData = new HashMap<>();
        posData.put("positions", positions);
        return posData;
    }

    private List<Map<String, Object>> createPosStatusList(final BotInstance instance) {
        List<PosTerminalConnection> connections = instance.getAllPosConnections();
        int threshold = config.getPosFailureThreshold();
        List<Map<String, Object>> positions = new ArrayList<>();

        for (PosTerminalConnection connection : connections) {
            Map<String, Object> posStatus = createSinglePosStatus(connection, threshold);
            positions.add(posStatus);
        }
        return positions;
    }

    private Map<String, Object> createSinglePosStatus(
            final PosTerminalConnection connection,
            final int threshold
    ) {
        HealthStatus healthStatus = connection.getHealthStatus(threshold);
        String ledColor = convertHealthStatusToLedColor(healthStatus);

        Map<String, Object> pos = new HashMap<>();
        pos.put("name", connection.getPosUid());
        pos.put("isActive", connection.isRunning());
        pos.put("ledColor", ledColor);
        pos.put("failCount", connection.getConsecutiveFailures());
        pos.put("lastUpdate", System.currentTimeMillis());
        return pos;
    }

    private String convertHealthStatusToLedColor(final HealthStatus healthStatus) {
        if (healthStatus == HealthStatus.GREEN) {
            return "green";
        }
        if (healthStatus == HealthStatus.ORANGE) {
            return "yellow";
        }
        return "red";
    }

    private void logBotStatus(
            final String botUid,
            final boolean isActive,
            final String responseTime
    ) {
        log.debug("Bot status reported: {} (Active: {}, Response: {})",
                botUid, isActive, responseTime);
    }
}
