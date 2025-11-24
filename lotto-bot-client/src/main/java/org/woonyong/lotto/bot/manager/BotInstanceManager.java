package org.woonyong.lotto.bot.manager;

import org.springframework.stereotype.Component;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.domain.BotInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotInstanceManager {
    private final Map<String, BotInstance> botInstances;
    private final BotClientConfig config;

    public BotInstanceManager(final BotClientConfig config) {
        this.botInstances = new ConcurrentHashMap<>();
        this.config = config;
    }

    public BotInstance createInstance(
            final String botUid,
            final int purchaseIntervalMs,
            final int ticketsPerPurchase
    ) {
        BotInstance instance = new BotInstance(botUid, purchaseIntervalMs, ticketsPerPurchase);
        botInstances.put(botUid, instance);
        return instance;
    }

    public void removeInstance(final String botUid) {
        BotInstance instance = botInstances.remove(botUid);
        if (instance != null) {
            instance.stop();
        }
    }

    public void removeAllInstances() {
        for (BotInstance instance : botInstances.values()) {
            instance.stop();
        }
        botInstances.clear();
    }

    public Optional<BotInstance> getInstance(final String botUid) {
        return Optional.ofNullable(botInstances.get(botUid));
    }

    public List<BotInstance> getAllInstances() {
        return new ArrayList<>(botInstances.values());
    }

    public int getActiveCount() {
        return botInstances.size();
    }

    public boolean hasCapacity() {
        return botInstances.size() < config.getMaxCapacity();
    }

    public boolean isInstanceRunning(final String botUid) {
        BotInstance instance = botInstances.get(botUid);
        return instance != null && instance.isRunning();
    }
}
