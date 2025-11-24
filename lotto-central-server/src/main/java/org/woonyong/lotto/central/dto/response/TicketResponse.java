package org.woonyong.lotto.central.dto.response;

import java.time.LocalDateTime;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.core.domain.TicketStatus;
import org.woonyong.lotto.core.domain.TicketType;

public class TicketResponse {
  private final Long id;
  private final String ticketNumber;
  private final Long roundId;
  private final String posUid;
  private final String numbers;
  private final TicketType type;
  private final TicketStatus status;
  private final Integer winningRank;
  private final Integer winningAmount;
  private final LocalDateTime purchaseTime;

  private TicketResponse(
      final Long id,
      final String ticketNumber,
      final Long roundId,
      final String posUid,
      final String numbers,
      final TicketType type,
      final TicketStatus status,
      final Integer winningRank,
      final Integer winningAmount,
      final LocalDateTime purchaseTime) {
    this.id = id;
    this.ticketNumber = ticketNumber;
    this.roundId = roundId;
    this.posUid = posUid;
    this.numbers = numbers;
    this.type = type;
    this.status = status;
    this.winningRank = winningRank;
    this.winningAmount = winningAmount;
    this.purchaseTime = purchaseTime;
  }

  public static TicketResponse from(final Ticket ticket) {
    return new TicketResponse(
        ticket.getId(),
        ticket.getTicketNumber(),
        ticket.getRoundId(),
        ticket.getPosUid(),
        ticket.getNumbers(),
        ticket.getType(),
        ticket.getStatus(),
        ticket.getWinningRank(),
        ticket.getWinningAmount(),
        ticket.getPurchaseTime());
  }

  public Long getId() {
    return id;
  }

  public String getTicketNumber() {
    return ticketNumber;
  }

  public Long getRoundId() {
    return roundId;
  }

  public String getPosUid() {
    return posUid;
  }

  public String getNumbers() {
    return numbers;
  }

  public TicketType getType() {
    return type;
  }

  public TicketStatus getStatus() {
    return status;
  }

  public Integer getWinningRank() {
    return winningRank;
  }

  public Integer getWinningAmount() {
    return winningAmount;
  }

  public LocalDateTime getPurchaseTime() {
    return purchaseTime;
  }
}
