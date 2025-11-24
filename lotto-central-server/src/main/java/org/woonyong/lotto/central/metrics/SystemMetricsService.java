package org.woonyong.lotto.central.metrics;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;
import org.woonyong.lotto.central.repository.BotRepository;
import org.woonyong.lotto.central.repository.PosRepository;
import org.woonyong.lotto.central.repository.TicketRepository;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemMetricsService {
    private static final long TICKET_PRICE = 1000L;

    private final MetricsCollector metricsCollector;
    private final TicketRepository ticketRepository;
    private final BotRepository botRepository;
    private final PosRepository posRepository;
    private final DataSource dataSource;
    private final long startTime;

    public SystemMetricsService(
            final MetricsCollector metricsCollector,
            final TicketRepository ticketRepository,
            final BotRepository botRepository,
            final PosRepository posRepository,
            final DataSource dataSource
    ) {
        this.metricsCollector = metricsCollector;
        this.ticketRepository = ticketRepository;
        this.botRepository = botRepository;
        this.posRepository = posRepository;
        this.dataSource = dataSource;
        this.startTime = System.currentTimeMillis();
    }

    public Map<String, Object> collectAllMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        addBusinessMetrics(metrics);
        addPerformanceMetrics(metrics);
        addSystemMetrics(metrics);
        addStatusMetrics(metrics);
        return metrics;
    }

    private void addBusinessMetrics(final Map<String, Object> metrics) {
        long totalGames = ticketRepository.count();
        long firstWinners = ticketRepository.countFirstPrizeWinners();
        long totalRevenue = totalGames * TICKET_PRICE;

        metrics.put("centralServerBalance", totalRevenue);
        metrics.put("totalGames", totalGames);
        metrics.put("totalFirstWinners", firstWinners);
    }

    private void addPerformanceMetrics(final Map<String, Object> metrics) {
        MetricsSnapshot snapshot = metricsCollector.calculateMetrics();
        metrics.put("tps", snapshot.getFormattedTps());
        metrics.put("avgLatency", snapshot.getFormattedLatency());
        metrics.put("successRate", snapshot.getFormattedSuccessRate());
        metrics.put("errorCount", snapshot.errorCount());
    }

    private void addSystemMetrics(final Map<String, Object> metrics) {
        long uptimeMs = System.currentTimeMillis() - startTime;
        metrics.put("uptime", uptimeMs);

        double cpuUsage = getCpuUsage();
        metrics.put("processCpuUsage", cpuUsage);
        metrics.put("systemCpuUsage", getSystemCpuUsage());

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryBean.getHeapMemoryUsage().getMax();
        double heapUsagePercent = (double) heapUsed / heapMax * 100;
        metrics.put("heapMemoryUsage", heapUsagePercent);

        metrics.put("dbPoolStatus", getDbPoolStatus());
    }

    private void addStatusMetrics(final Map<String, Object> metrics) {
        long activeBots = botRepository.countByActiveTrue();
        long totalBots = botRepository.count();
        metrics.put("botStatus", activeBots + "/" + totalBots);

        long activePos = posRepository.countByActiveTrue();
        long totalPos = posRepository.count();
        metrics.put("posStatus", activePos + "/" + totalPos);
    }

    private double getCpuUsage() {
        OperatingSystemMXBean osBean = getOsBean();
        if (osBean == null) {
            return 0.0;
        }
        return osBean.getProcessCpuLoad() * 100;
    }

    private double getSystemCpuUsage() {
        OperatingSystemMXBean osBean = getOsBean();
        if (osBean == null) {
            return 0.0;
        }
        return osBean.getCpuLoad() * 100;
    }

    private OperatingSystemMXBean getOsBean() {
        return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    private String getDbPoolStatus() {
        try {
            if (dataSource instanceof com.zaxxer.hikari.HikariDataSource hikariDs) {
                com.zaxxer.hikari.HikariPoolMXBean poolBean = hikariDs.getHikariPoolMXBean();
                int active = poolBean.getActiveConnections();
                int total = poolBean.getTotalConnections();
                int max = hikariDs.getMaximumPoolSize();
                return active + "/" + max;
            }
            return "0/100";
        } catch (Exception e) {
            return "0/100";
        }
    }
}
