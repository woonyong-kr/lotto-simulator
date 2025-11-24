package org.woonyong.lotto.bot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.domain.BotInstance;
import org.woonyong.lotto.bot.dto.response.BotStatusResponse;
import org.woonyong.lotto.bot.manager.BotInstanceManager;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stream")
public class BotSseController {

    private final BotInstanceManager botInstanceManager;
    private final BotClientConfig config;

    public BotSseController(
            final BotInstanceManager botInstanceManager,
            final BotClientConfig config
    ) {
        this.botInstanceManager = botInstanceManager;
        this.config = config;
    }

    @GetMapping(value = "/bots", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BotStatusResponse> streamBotStatus() {
        return Flux.interval(Duration.ofMillis(config.getSseIntervalMs()))
                .map(tick -> createBotStatusResponse());
    }

    private BotStatusResponse createBotStatusResponse() {
        List<BotInstance> instances = botInstanceManager.getAllInstances();
        collectResponseTimesFromAllBots(instances);
        List<BotStatusResponse.BotInfo> botInfos = createBotInfoList(instances);
        clearResponseTimesFromAllBots(instances);
        return new BotStatusResponse(
                config.getMaxCapacity(),
                botInstanceManager.getActiveCount(),
                botInfos
        );
    }

    private void collectResponseTimesFromAllBots(final List<BotInstance> instances) {
        for (BotInstance instance : instances) {
            instance.collectResponseTimesFromPos();
        }
    }

    private List<BotStatusResponse.BotInfo> createBotInfoList(
            final List<BotInstance> instances
    ) {
        int threshold = config.getPosFailureThreshold();
        List<BotStatusResponse.BotInfo> botInfos = new ArrayList<>();
        for (BotInstance instance : instances) {
            botInfos.add(BotStatusResponse.BotInfo.from(instance, threshold));
        }
        return botInfos;
    }

    private void clearResponseTimesFromAllBots(final List<BotInstance> instances) {
        for (BotInstance instance : instances) {
            instance.clearAggregatedResponseTimes();
        }
    }
}
