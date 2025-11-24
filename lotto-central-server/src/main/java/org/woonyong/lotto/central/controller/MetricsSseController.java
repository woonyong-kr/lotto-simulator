package org.woonyong.lotto.central.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.central.config.RoundConfig;
import org.woonyong.lotto.central.metrics.SystemMetricsService;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/sse/metrics")
public class MetricsSseController {

    private final SystemMetricsService systemMetricsService;
    private final RoundConfig roundConfig;

    public MetricsSseController(
            final SystemMetricsService systemMetricsService,
            final RoundConfig roundConfig
    ) {
        this.systemMetricsService = systemMetricsService;
        this.roundConfig = roundConfig;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamMetrics() {
        return Flux.interval(Duration.ofMillis(roundConfig.getSseInterval()))
                .map(tick -> systemMetricsService.collectAllMetrics());
    }
}
