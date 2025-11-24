package org.woonyong.lotto.bot.dto.request;

public class AllocateTerminalRequest {
  private String botUid;
  private String posUid;

  public AllocateTerminalRequest() {}

  public AllocateTerminalRequest(final String botUid, final String posUid) {
    this.botUid = botUid;
    this.posUid = posUid;
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
