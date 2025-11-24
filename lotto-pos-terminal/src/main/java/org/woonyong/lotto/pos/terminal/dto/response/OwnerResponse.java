package org.woonyong.lotto.pos.terminal.dto.response;

import java.time.LocalDateTime;

public class OwnerResponse {
  private final String terminalId;
  private final LocalDateTime assignedAt;

  public OwnerResponse(final String terminalId, final LocalDateTime assignedAt) {
    this.terminalId = terminalId;
    this.assignedAt = assignedAt;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public LocalDateTime getAssignedAt() {
    return assignedAt;
  }
}
