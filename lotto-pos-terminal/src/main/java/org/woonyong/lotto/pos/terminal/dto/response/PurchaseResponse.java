package org.woonyong.lotto.pos.terminal.dto.response;

public class PurchaseResponse {
  private final String ticketNumber;
  private final long responseTime;

  public PurchaseResponse(final String ticketNumber, final long responseTime) {
    this.ticketNumber = ticketNumber;
    this.responseTime = responseTime;
  }

  public String getTicketNumber() {
    return ticketNumber;
  }

  public long getResponseTime() {
    return responseTime;
  }
}
