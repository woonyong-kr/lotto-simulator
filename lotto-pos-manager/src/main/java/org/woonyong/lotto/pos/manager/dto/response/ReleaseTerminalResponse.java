package org.woonyong.lotto.pos.manager.dto.response;

public class ReleaseTerminalResponse {
  private final boolean released;
  private final int availableCount;

  public ReleaseTerminalResponse(final boolean released, final int availableCount) {
    this.released = released;
    this.availableCount = availableCount;
  }

  public boolean isReleased() {
    return released;
  }

  public int getAvailableCount() {
    return availableCount;
  }
}
