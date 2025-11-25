package org.woonyong.lotto.bot.domain;

import java.util.ArrayList;
import java.util.List;

public class PosTerminalConnection {
  private final String posUid;
  private String terminalId;
  private String terminalAddress;
  private int consecutiveFailures;
  private boolean running;
  private final List<Long> responseTimes;

  public PosTerminalConnection(final String posUid, final String terminalId, final String terminalAddress) {
    this.posUid = posUid;
    this.terminalId = terminalId;
    this.terminalAddress = terminalAddress;
    this.consecutiveFailures = 0;
    this.running = true;
    this.responseTimes = new ArrayList<>();
  }

  public void recordSuccess(final long responseTimeMs) {
    this.consecutiveFailures = 0;
    this.responseTimes.add(responseTimeMs);
  }

  public void recordFailure() {
    this.consecutiveFailures++;
  }

  public void markAsDown() {
    this.running = false;
  }

  public void reconnect(final String newTerminalId, final String newTerminalAddress) {
    this.terminalId = newTerminalId;
    this.terminalAddress = newTerminalAddress;
    this.consecutiveFailures = 0;
    this.running = true;
  }

  public Long calculateAverageResponseTime() {
    if (responseTimes.isEmpty()) {
      return null;
    }
    long sum = responseTimes.stream().mapToLong(Long::longValue).sum();
    return sum / responseTimes.size();
  }

  public void clearResponseTimes() {
    this.responseTimes.clear();
  }

  public HealthStatus getHealthStatus(final int threshold) {
    return HealthStatus.fromFailureCount(consecutiveFailures, threshold);
  }

  public String getPosUid() {
    return posUid;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public String getTerminalAddress() {
    return terminalAddress;
  }

  public int getConsecutiveFailures() {
    return consecutiveFailures;
  }

  public boolean isRunning() {
    return running;
  }
}
