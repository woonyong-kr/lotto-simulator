package org.woonyong.lotto.central.entity;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bots")
public class Bot {
  private static final String BOT_UID_PREFIX = "BOT-";
  private static final String BOT_UID_FORMAT = "%08d";
  private static final String POS_UID_DELIMITER = ",";
  private static final int DEFAULT_PURCHASE_INTERVAL_MS = 1000;
  private static final int DEFAULT_TICKETS_PER_PURCHASE = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bot_uid", nullable = false, unique = true, length = 20)
  private String botUid;

  @Column(name = "pos_uids", nullable = false, length = 200)
  private String posUids;

  @Column(name = "purchase_interval_ms", nullable = false)
  private int purchaseIntervalMs;

  @Column(name = "tickets_per_purchase", nullable = false)
  private int ticketsPerPurchase;

  @Column(name = "active", nullable = false)
  private boolean active;

  protected Bot() {}

  public static Bot create(final long sequenceNumber, final List<String> posUidList) {
    Bot bot = new Bot();
    bot.botUid = generateBotUid(sequenceNumber);
    bot.posUids = String.join(POS_UID_DELIMITER, posUidList);
    bot.purchaseIntervalMs = DEFAULT_PURCHASE_INTERVAL_MS;
    bot.ticketsPerPurchase = DEFAULT_TICKETS_PER_PURCHASE;
    bot.active = true;
    return bot;
  }

  public void updateConfig(final int purchaseIntervalMs, final int ticketsPerPurchase) {
    this.purchaseIntervalMs = purchaseIntervalMs;
    this.ticketsPerPurchase = ticketsPerPurchase;
  }

  public void deactivate() {
    this.active = false;
  }

  public void addPosUid(final String posUid) {
    if (posUids.isEmpty()) {
      this.posUids = posUid;
      return;
    }
    this.posUids = posUids + POS_UID_DELIMITER + posUid;
  }

  public void removePosUid(final String posUid) {
    List<String> currentList = getPosUidList();
    List<String> updatedList = currentList.stream().filter(uid -> !uid.equals(posUid)).toList();
    this.posUids = String.join(POS_UID_DELIMITER, updatedList);
  }

  public List<String> getPosUidList() {
    if (posUids == null || posUids.isEmpty()) {
      return Collections.emptyList();
    }
    return Arrays.asList(posUids.split(POS_UID_DELIMITER));
  }

  private static String generateBotUid(final long sequenceNumber) {
    return BOT_UID_PREFIX + String.format(BOT_UID_FORMAT, sequenceNumber);
  }

  public Long getId() {
    return id;
  }

  public String getBotUid() {
    return botUid;
  }

  public String getPosUids() {
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Bot bot)) {
      return false;
    }
    return Objects.equals(id, bot.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
