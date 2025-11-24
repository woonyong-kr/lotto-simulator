package org.woonyong.lotto.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.woonyong.lotto.bot.client.CentralServerClient;
import org.woonyong.lotto.bot.client.PosManagerClient;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.domain.BotInstance;
import org.woonyong.lotto.bot.dto.response.AllocateTerminalResponse;
import org.woonyong.lotto.bot.dto.response.BotDataResponse;
import org.woonyong.lotto.bot.manager.BotInstanceManager;

import java.util.List;
import java.util.Optional;

@Service
public class BotService {
    private static final Logger log = LoggerFactory.getLogger(BotService.class);

    private final BotInstanceManager botInstanceManager;
    private final CentralServerClient centralServerClient;
    private final PosManagerClient posManagerClient;
    private final BotClientConfig config;

    public BotService(
            final BotInstanceManager botInstanceManager,
            final CentralServerClient centralServerClient,
            final PosManagerClient posManagerClient,
            final BotClientConfig config
    ) {
        this.botInstanceManager = botInstanceManager;
        this.centralServerClient = centralServerClient;
        this.posManagerClient = posManagerClient;
        this.config = config;
    }

    public String createBot() {
        if (!botInstanceManager.hasCapacity()) {
            log.warn("봇 최대 용량 초과");
            return null;
        }
        BotDataResponse botData = centralServerClient.createBot();
        if (botData == null) {
            return null;
        }
        return initializeBotInstance(botData);
    }

    public boolean updateConfig(
            final String botUid,
            final int purchaseIntervalMs,
            final int ticketsPerPurchase
    ) {
        Optional<BotInstance> instanceOpt = botInstanceManager.getInstance(botUid);
        if (instanceOpt.isEmpty()) {
            return false;
        }
        instanceOpt.get().updateConfig(purchaseIntervalMs, ticketsPerPurchase);
        return true;
    }

    public boolean deleteBot(final String botUid) {
        Optional<BotInstance> instanceOpt = botInstanceManager.getInstance(botUid);
        if (instanceOpt.isEmpty()) {
            return false;
        }
        centralServerClient.deactivateBot(botUid);
        botInstanceManager.removeInstance(botUid);
        return true;
    }

    public int deleteAllBots() {
        List<BotInstance> instances = botInstanceManager.getAllInstances();
        int count = instances.size();
        for (BotInstance instance : instances) {
            centralServerClient.deactivateBot(instance.getBotUid());
        }
        botInstanceManager.removeAllInstances();
        return count;
    }

    public String initializeBotInstance(final BotDataResponse botData) {
        BotInstance instance = botInstanceManager.createInstance(
                botData.getBotUid(),
                botData.getPurchaseIntervalMs(),
                botData.getTicketsPerPurchase()
        );
        allocateTerminalsForBot(instance, botData);
        log.info("봇 인스턴스 생성 완료: {}", botData.getBotUid());
        return botData.getBotUid();
    }

    private void allocateTerminalsForBot(
            final BotInstance instance,
            final BotDataResponse botData
    ) {
        for (String posUid : botData.getPosUids()) {
            boolean allocated = tryAllocateTerminal(instance, botData.getBotUid(), posUid);
            if (!allocated) {
                deactivatePosUntilSuccess(posUid);
            }
        }
    }

    private boolean tryAllocateTerminal(
            final BotInstance instance,
            final String botUid,
            final String posUid
    ) {
        int maxRetry = config.getPosAllocationMaxRetry();
        for (int attempt = 1; attempt <= maxRetry; attempt++) {
            if (attemptAllocation(instance, botUid, posUid)) {
                return true;
            }
            logAllocationFailure(attempt, maxRetry, posUid);
            sleepQuietly(config.getPosReallocationRetryMs());
        }
        log.error("터미널 할당 최종 실패: posUid={}", posUid);
        return false;
    }

    private boolean attemptAllocation(
            final BotInstance instance,
            final String botUid,
            final String posUid
    ) {
        AllocateTerminalResponse response = posManagerClient.allocateTerminal(botUid, posUid);
        if (response == null || !response.isSuccess()) {
            return false;
        }
        instance.addPosConnection(posUid, response.getAddress());
        log.debug("터미널 할당 성공: botUid={}, posUid={}", botUid, posUid);
        return true;
    }

    private void logAllocationFailure(
            final int attempt,
            final int maxRetry,
            final String posUid
    ) {
        log.warn("터미널 할당 실패 (시도 {}/{}): posUid={}", attempt, maxRetry, posUid);
    }

    private void deactivatePosUntilSuccess(final String posUid) {
        while (!tryDeactivatePos(posUid)) {
            log.warn("POS 비활성화 실패, 재시도 대기: posUid={}", posUid);
            sleepQuietly(config.getPosReallocationRetryMs());
        }
        log.info("POS 비활성화 성공: posUid={}", posUid);
    }

    private boolean tryDeactivatePos(final String posUid) {
        return centralServerClient.deactivatePos(posUid);
    }

    private void sleepQuietly(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
