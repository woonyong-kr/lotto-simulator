package org.woonyong.lotto.bot.dto.request;

public class AllocateTerminalRequest {
  private String botUid;
  private String posUid;
  private String botClientUrl;

  public AllocateTerminalRequest() {}

  public AllocateTerminalRequest(final String botUid, final String posUid, final String botClientUrl) {
    this.botUid = botUid;
    this.posUid = posUid;
    this.botClientUrl = botClientUrl;
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

  public String getBotClientUrl() {
    return botClientUrl;
  }

  public void setBotClientUrl(final String botClientUrl) {
    this.botClientUrl = botClientUrl;
  }
}
