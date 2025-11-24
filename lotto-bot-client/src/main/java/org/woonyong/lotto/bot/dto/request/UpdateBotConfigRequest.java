package org.woonyong.lotto.bot.dto.request;

public class UpdateBotConfigRequest {
  private int purchaseIntervalMs;
  private int ticketsPerPurchase;

  public UpdateBotConfigRequest() {}

  public UpdateBotConfigRequest(final int purchaseIntervalMs, final int ticketsPerPurchase) {
    this.purchaseIntervalMs = purchaseIntervalMs;
    this.ticketsPerPurchase = ticketsPerPurchase;
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
}
