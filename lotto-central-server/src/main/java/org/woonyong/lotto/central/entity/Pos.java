package org.woonyong.lotto.central.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "poses")
public class Pos {
  private static final String POS_UID_PREFIX = "POS-";
  private static final String POS_UID_FORMAT = "%06d";
  private static final int TICKET_PRICE = 1000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "pos_uid", nullable = false, unique = true, length = 10)
  private String posUid;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Column(name = "total_tickets", nullable = false)
  private long totalTickets;

  @Column(name = "total_sales", nullable = false)
  private long totalSales;

  @Column(name = "total_winnings", nullable = false)
  private long totalWinnings;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected Pos() {}

  public static Pos create(final long sequenceNumber) {
    Pos pos = new Pos();
    pos.posUid = generatePosUid(sequenceNumber);
    pos.active = true;
    pos.totalTickets = 0;
    pos.totalSales = 0;
    pos.totalWinnings = 0;
    pos.createdAt = LocalDateTime.now();
    pos.updatedAt = LocalDateTime.now();
    return pos;
  }

  public void addSales(final int ticketCount) {
    this.totalTickets += ticketCount;
    this.totalSales += (long) ticketCount * TICKET_PRICE;
    this.updatedAt = LocalDateTime.now();
  }

  public void addWinnings(final long amount) {
    this.totalWinnings += amount;
    this.updatedAt = LocalDateTime.now();
  }

  public void activate() {
    this.active = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void deactivate() {
    this.active = false;
    this.updatedAt = LocalDateTime.now();
  }

  private static String generatePosUid(final long sequenceNumber) {
    return POS_UID_PREFIX + String.format(POS_UID_FORMAT, sequenceNumber);
  }

  public Long getId() {
    return id;
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pos pos)) {
      return false;
    }
    return Objects.equals(id, pos.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
