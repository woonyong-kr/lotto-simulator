package org.woonyong.lotto.bot.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.bot.client.CentralServerClient;
import org.woonyong.lotto.bot.dto.response.BotDataResponse;
import org.woonyong.lotto.bot.service.BotService;

import java.util.List;

@Component
public class BotClientInitializer {
    private static final Logger log = LoggerFactory.getLogger(BotClientInitializer.class);

    private final CentralServerClient centralServerClient;
    private final BotService botService;
    private final BotClientConfig config;

    public BotClientInitializer(
            final CentralServerClient centralServerClient,
            final BotService botService,
            final BotClientConfig config
    ) {
        this.centralServerClient = centralServerClient;
        this.botService = botService;
        this.config = config;
    }

    @PostConstruct
    public void initialize() {
        log.info("봇 클라이언트 초기화 시작");
        List<BotDataResponse> activeBots = centralServerClient.getActiveBots();
        log.info("활성 봇 조회 완료: {}개", activeBots.size());
        initializeBotsWithCapacityCheck(activeBots);
        log.info("봇 클라이언트 초기화 완료");
    }

    private void initializeBotsWithCapacityCheck(final List<BotDataResponse> activeBots) {
        int maxCapacity = config.getMaxCapacity();
        int count = 0;
        for (BotDataResponse botData : activeBots) {
            if (count >= maxCapacity) {
                deactivateExcessBot(botData);
                continue;
            }
            botService.initializeBotInstance(botData);
            count++;
        }
    }

    private void deactivateExcessBot(final BotDataResponse botData) {
        log.warn("최대 용량 초과로 봇 비활성화: {}", botData.getBotUid());
        centralServerClient.deactivateBot(botData.getBotUid());
    }
}
