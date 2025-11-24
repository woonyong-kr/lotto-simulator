package org.woonyong.lotto.bot.dto.response;

public class UpdateBotConfigResponse {
  private final String botUid;
  private final int purchaseIntervalMs;
  private final int ticketsPerPurchase;

  public UpdateBotConfigResponse(
      final String botUid, final int purchaseIntervalMs, final int ticketsPerPurchase) {
    this.botUid = botUid;
    this.purchaseIntervalMs = purchaseIntervalMs;
    this.ticketsPerPurchase = ticketsPerPurchase;
  }

  public String getBotUid() {
    return botUid;
  }

  public int getPurchaseIntervalMs() {
    return purchaseIntervalMs;
  }

  public int getTicketsPerPurchase() {
    return ticketsPerPurchase;
  }
}
