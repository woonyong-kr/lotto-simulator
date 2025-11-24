package org.woonyong.lotto.central.metrics;

public record MetricsSnapshot(
        long tps,
        long avgLatencyMs,
        double successRate,
        int errorCount
) {
    public String getFormattedTps() {
        return tps + "/s";
    }

    public String getFormattedLatency() {
        return avgLatencyMs + "ms";
    }

    public String getFormattedSuccessRate() {
        return String.format("%.2f%%", successRate);
    }
}
