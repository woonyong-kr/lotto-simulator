package org.woonyong.lotto.central.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.woonyong.lotto.central.config.RoundConfig;
import org.woonyong.lotto.central.service.RoundStatisticsService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sse/rounds")
public class RoundSseController {

    private final RoundStatisticsService roundStatisticsService;
    private final RoundConfig roundConfig;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public RoundSseController(
            final RoundStatisticsService roundStatisticsService,
            final RoundConfig roundConfig
    ) {
        this.roundStatisticsService = roundStatisticsService;
        this.roundConfig = roundConfig;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamRounds() {
        SseEmitter emitter = new SseEmitter();
        scheduleEmitter(emitter);
        return emitter;
    }

    private void scheduleEmitter(final SseEmitter emitter) {
        executor.scheduleAtFixedRate(
                () -> sendRoundData(emitter),
                0,
                roundConfig.getSseInterval(),
                TimeUnit.MILLISECONDS
        );
    }

    private void sendRoundData(final SseEmitter emitter) {
        try {
            emitter.send(roundStatisticsService.getRecentRounds());
        } catch (IOException e) {
            emitter.complete();
        }
    }
}