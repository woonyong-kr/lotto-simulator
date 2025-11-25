package org.woonyong.lotto.bot.dto.request;

public class OwnerValidationRequest {
  private String ownerBotUid;
  private String posUid;
  private String terminalId;

  public OwnerValidationRequest() {}

  public OwnerValidationRequest(final String ownerBotUid, final String posUid, final String terminalId) {
    this.ownerBotUid = ownerBotUid;
    this.posUid = posUid;
    this.terminalId = terminalId;
  }

  public String getOwnerBotUid() {
    return ownerBotUid;
  }

  public void setOwnerBotUid(final String ownerBotUid) {
    this.ownerBotUid = ownerBotUid;
  }

  public String getPosUid() {
    return posUid;
  }

  public void setPosUid(final String posUid) {
    this.posUid = posUid;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(final String terminalId) {
    this.terminalId = terminalId;
  }
}