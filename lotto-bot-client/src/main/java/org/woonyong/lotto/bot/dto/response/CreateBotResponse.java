package org.woonyong.lotto.bot.dto.response;

public class CreateBotResponse {
  private final String botUid;

  public CreateBotResponse(final String botUid) {
    this.botUid = botUid;
  }

  public String getBotUid() {
    return botUid;
  }
}
