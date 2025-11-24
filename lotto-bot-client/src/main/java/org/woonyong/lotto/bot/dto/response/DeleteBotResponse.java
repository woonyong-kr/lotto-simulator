package org.woonyong.lotto.bot.dto.response;

public class DeleteBotResponse {
  private final String botUid;

  public DeleteBotResponse(final String botUid) {
    this.botUid = botUid;
  }

  public String getBotUid() {
    return botUid;
  }
}
