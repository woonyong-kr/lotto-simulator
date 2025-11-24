package org.woonyong.lotto.central.metrics;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MetricsCollector {
    private static final int WINDOW_SIZE_SECONDS = 5;
    private static final double EMA_ALPHA = 0.3;

    private final Queue<RequestRecord> requestRecords = new ConcurrentLinkedQueue<>();
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successRequests = new AtomicLong(0);

    private volatile double emaTps = 0.0;
    private volatile double emaLatency = 0.0;

    public void recordRequest(final long responseTimeMs, final boolean success) {
        long now = System.currentTimeMillis();
        requestRecords.add(new RequestRecord(now, responseTimeMs, success));
        totalRequests.incrementAndGet();

        if (success) {
            successRequests.incrementAndGet();
        } else {
            errorCount.incrementAndGet();
        }
    }

    public MetricsSnapshot calculateMetrics() {
        removeOldRecords();
        double[] currentMetrics = calculateCurrentMetrics();
        updateEmaValues(currentMetrics[0], currentMetrics[1]);
        return createSnapshot();
    }

    private void removeOldRecords() {
        long cutoff = System.currentTimeMillis() - (WINDOW_SIZE_SECONDS * 1000L);
        while (!requestRecords.isEmpty()) {
            RequestRecord oldest = requestRecords.peek();
            if (oldest == null || oldest.timestamp() >= cutoff) {
                break;
            }
            requestRecords.poll();
        }
    }

    private double[] calculateCurrentMetrics() {
        int count = 0;
        long totalLatency = 0;

        for (RequestRecord record : requestRecords) {
            count++;
            totalLatency += record.responseTimeMs();
        }

        double currentTps = (double) count / WINDOW_SIZE_SECONDS;
        double currentLatency = count > 0 ? (double) totalLatency / count : 0;
        return new double[]{currentTps, currentLatency};
    }

    private void updateEmaValues(final double currentTps, final double currentLatency) {
        if (emaTps == 0.0) {
            emaTps = currentTps;
            emaLatency = currentLatency;
            return;
        }
        emaTps = EMA_ALPHA * currentTps + (1 - EMA_ALPHA) * emaTps;
        emaLatency = EMA_ALPHA * currentLatency + (1 - EMA_ALPHA) * emaLatency;
    }

    private MetricsSnapshot createSnapshot() {
        double successRate = calculateSuccessRate();
        return new MetricsSnapshot(
                Math.round(emaTps),
                Math.round(emaLatency),
                successRate,
                errorCount.get()
        );
    }

    private double calculateSuccessRate() {
        long total = totalRequests.get();
        if (total == 0) {
            return 100.0;
        }
        return (double) successRequests.get() / total * 100;
    }

    public void resetErrorCount() {
        errorCount.set(0);
    }

    private record RequestRecord(long timestamp, long responseTimeMs, boolean success) {
    }
}
