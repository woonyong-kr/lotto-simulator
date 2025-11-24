package org.woonyong.lotto.bot.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotInstance {
  private final String botUid;
  private int purchaseIntervalMs;
  private int ticketsPerPurchase;
  private boolean running;
  private final Map<String, PosTerminalConnection> posConnections;
  private final List<Long> aggregatedResponseTimes;

  public BotInstance(
      final String botUid, final int purchaseIntervalMs, final int ticketsPerPurchase) {
    this.botUid = botUid;
    this.purchaseIntervalMs = purchaseIntervalMs;
    this.ticketsPerPurchase = ticketsPerPurchase;
    this.running = true;
    this.posConnections = new ConcurrentHashMap<>();
    this.aggregatedResponseTimes = new ArrayList<>();
  }

  public void addPosConnection(final String posUid, final String terminalAddress) {
    PosTerminalConnection connection = new PosTerminalConnection(posUid, terminalAddress);
    posConnections.put(posUid, connection);
  }

  public void updateConfig(final int purchaseIntervalMs, final int ticketsPerPurchase) {
    this.purchaseIntervalMs = purchaseIntervalMs;
    this.ticketsPerPurchase = ticketsPerPurchase;
  }

  public void stop() {
    this.running = false;
  }

  public void collectResponseTimesFromPos() {
    for (PosTerminalConnection connection : posConnections.values()) {
      Long avgTime = connection.calculateAverageResponseTime();
      if (avgTime != null) {
        aggregatedResponseTimes.add(avgTime);
      }
      connection.clearResponseTimes();
    }
  }

  public Long calculateAverageResponseTime() {
    if (aggregatedResponseTimes.isEmpty()) {
      return null;
    }
    long sum = aggregatedResponseTimes.stream().mapToLong(Long::longValue).sum();
    return sum / aggregatedResponseTimes.size();
  }

  public void clearAggregatedResponseTimes() {
    this.aggregatedResponseTimes.clear();
  }

  public PosTerminalConnection getPosConnection(final String posUid) {
    return posConnections.get(posUid);
  }

  public List<PosTerminalConnection> getAllPosConnections() {
    return new ArrayList<>(posConnections.values());
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

  public boolean isRunning() {
    return running;
  }
}
