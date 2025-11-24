package org.woonyong.lotto.pos.manager.dto.response;

public class AllocateTerminalResponse {
  private final String terminalId;
  private final String address;

  public AllocateTerminalResponse(final String terminalId, final String address) {
    this.terminalId = terminalId;
    this.address = address;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public String getAddress() {
    return address;
  }
}
