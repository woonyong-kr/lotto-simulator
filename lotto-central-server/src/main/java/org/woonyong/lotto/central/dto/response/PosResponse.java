package org.woonyong.lotto.central.dto.response;

import java.time.LocalDateTime;
import org.woonyong.lotto.central.entity.Pos;

public class PosResponse {
  private final String posUid;
  private final boolean active;
  private final long totalTickets;
  private final long totalSales;
  private final long totalWinnings;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  private PosResponse(
      final String posUid,
      final boolean active,
      final long totalTickets,
      final long totalSales,
      final long totalWinnings,
      final LocalDateTime createdAt,
      final LocalDateTime updatedAt) {
    this.posUid = posUid;
    this.active = active;
    this.totalTickets = totalTickets;
    this.totalSales = totalSales;
    this.totalWinnings = totalWinnings;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static PosResponse from(final Pos pos) {
    return new PosResponse(
        pos.getPosUid(),
        pos.isActive(),
        pos.getTotalTickets(),
        pos.getTotalSales(),
        pos.getTotalWinnings(),
        pos.getCreatedAt(),
        pos.getUpdatedAt());
  }

  public String getPosUid() {
    return posUid;
  }

  public boolean isActive() {
    return active;
  }

  public long getTotalTickets() {
    return totalTickets;
  }

  public long getTotalSales() {
    return totalSales;
  }

  public long getTotalWinnings() {
    return totalWinnings;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
