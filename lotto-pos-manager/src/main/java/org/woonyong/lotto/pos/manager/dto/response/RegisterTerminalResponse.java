package org.woonyong.lotto.pos.manager.dto.response;

public class RegisterTerminalResponse {
  private final boolean registered;
  private final int availableCount;

  public RegisterTerminalResponse(final boolean registered, final int availableCount) {
    this.registered = registered;
    this.availableCount = availableCount;
  }

  public boolean isRegistered() {
    return registered;
  }

  public int getAvailableCount() {
    return availableCount;
  }
}
