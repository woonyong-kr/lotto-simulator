package org.woonyong.lotto.pos.terminal.service;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class OwnerManager {
  private String ownerBotUid;
  private String posUid;
  private String botClientUrl;
  private LocalDateTime assignedAt;
  private int consecutiveFailures;

  public boolean hasOwner() {
    return ownerBotUid != null && posUid != null;
  }

  public void assignOwner(final String botUid, final String posUid, final String botClientUrl) {
    this.ownerBotUid = botUid;
    this.posUid = posUid;
    this.botClientUrl = botClientUrl;
    this.assignedAt = LocalDateTime.now();
    this.consecutiveFailures = 0;
  }

  public void clearOwner() {
    this.ownerBotUid = null;
    this.posUid = null;
    this.botClientUrl = null;
    this.assignedAt = null;
    this.consecutiveFailures = 0;
  }

  public boolean matches(final String botUid, final String posUid) {
    return Objects.equals(this.ownerBotUid, botUid) && Objects.equals(this.posUid, posUid);
  }

  public String getOwnerBotUid() {
    return ownerBotUid;
  }

  public String getPosUid() {
    return posUid;
  }

  public LocalDateTime getAssignedAt() {
    return assignedAt;
  }

  public String getBotClientUrl() {
    return botClientUrl;
  }

  public int getConsecutiveFailures() {
    return consecutiveFailures;
  }

  public void incrementFailure() {
    this.consecutiveFailures++;
  }

  public void resetFailures() {
    this.consecutiveFailures = 0;
  }
}
