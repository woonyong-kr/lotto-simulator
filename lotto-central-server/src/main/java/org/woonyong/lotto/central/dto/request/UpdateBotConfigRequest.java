package org.woonyong.lotto.central.dto.request;

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

  public int getTicketsPerPurchase() {
    return ticketsPerPurchase;
  }
}
