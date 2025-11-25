package org.woonyong.lotto.bot.dto.request;

public class ReleaseTerminalRequest {
  private String terminalId;
  private String botUid;
  private String posUid;

  public ReleaseTerminalRequest() {}

  public ReleaseTerminalRequest(final String terminalId, final String botUid, final String posUid) {
    this.terminalId = terminalId;
    this.botUid = botUid;
    this.posUid = posUid;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(final String terminalId) {
    this.terminalId = terminalId;
  }

  public String getBotUid() {
    return botUid;
  }

  public void setBotUid(final String botUid) {
    this.botUid = botUid;
  }

  public String getPosUid() {
    return posUid;
  }

  public void setPosUid(final String posUid) {
    this.posUid = posUid;
  }
}