package org.woonyong.lotto.bot.dto.response;

import java.util.List;

public class BotDataResponse {
    private String botUid;
    private List<String> posUids;
    private int purchaseIntervalMs;
    private int ticketsPerPurchase;
    private boolean active;

    public BotDataResponse() {
    }

    public String getBotUid() {
        return botUid;
    }

    public void setBotUid(final String botUid) {
        this.botUid = botUid;
    }

    public List<String> getPosUids() {
        return posUids;
    }

    public void setPosUids(final List<String> posUids) {
        this.posUids = posUids;
    }

    public int getPurchaseIntervalMs() {
        return purchaseIntervalMs;
    }

    public void setPurchaseIntervalMs(final int purchaseIntervalMs) {
        this.purchaseIntervalMs = purchaseIntervalMs;
    }

    public int getTicketsPerPurchase() {
        return ticketsPerPurchase;
    }

    public void setTicketsPerPurchase(final int ticketsPerPurchase) {
        this.ticketsPerPurchase = ticketsPerPurchase;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
