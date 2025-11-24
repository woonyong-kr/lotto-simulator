package org.woonyong.lotto.central.dto.response;

import org.woonyong.lotto.central.entity.Bot;

import java.util.List;

public class BotResponse {
    private final String botUid;
    private final List<String> posUids;
    private final int purchaseIntervalMs;
    private final int ticketsPerPurchase;
    private final boolean active;

    private BotResponse(
            final String botUid,
            final List<String> posUids,
            final int purchaseIntervalMs,
            final int ticketsPerPurchase,
            final boolean active
    ) {
        this.botUid = botUid;
        this.posUids = posUids;
        this.purchaseIntervalMs = purchaseIntervalMs;
        this.ticketsPerPurchase = ticketsPerPurchase;
        this.active = active;
    }

    public static BotResponse from(final Bot bot) {
        return new BotResponse(
                bot.getBotUid(),
                bot.getPosUidList(),
                bot.getPurchaseIntervalMs(),
                bot.getTicketsPerPurchase(),
                bot.isActive()
        );
    }

    public String getBotUid() {
        return botUid;
    }

    public List<String> getPosUids() {
        return posUids;
    }

    public int getPurchaseIntervalMs() {
        return purchaseIntervalMs;
    }

    public int getTicketsPerPurchase() {
        return ticketsPerPurchase;
    }

    public boolean isActive() {
        return active;
    }
}
