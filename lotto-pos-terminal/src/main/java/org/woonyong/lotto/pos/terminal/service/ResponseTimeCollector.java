package org.woonyong.lotto.pos.terminal.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseTimeCollector {
    private final List<Long> responseTimesMs = new ArrayList<>();

    public void record(final long responseTimeMs) {
        synchronized (responseTimesMs) {
            responseTimesMs.add(responseTimeMs);
        }
    }

    public Long calculateAverageAndClear() {
        synchronized (responseTimesMs) {
            if (responseTimesMs.isEmpty()) {
                return null;
            }

            long sum = responseTimesMs.stream()
                    .mapToLong(Long::longValue)
                    .sum();
            long average = sum / responseTimesMs.size();

            responseTimesMs.clear();

            return average;
        }
    }
}