package org.woonyong.lotto.bot.dto.response;

public class AllocateTerminalResponse {
  private String terminalId;
  private String address;
  private boolean success;
  private String message;

  public AllocateTerminalResponse() {}

  public String getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(final String terminalId) {
    this.terminalId = terminalId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
