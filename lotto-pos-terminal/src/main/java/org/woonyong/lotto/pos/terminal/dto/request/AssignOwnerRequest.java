package org.woonyong.lotto.pos.terminal.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AssignOwnerRequest {
  @NotBlank(message = "봇 UID는 필수입니다")
  private String botUid;

  @NotBlank(message = "POS UID는 필수입니다")
  private String posUid;

  @NotBlank(message = "봇 클라이언트 URL은 필수입니다")
  private String botClientUrl;

  public AssignOwnerRequest() {}

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
