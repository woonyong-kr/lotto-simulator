package org.woonyong.lotto.pos.manager.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ReleaseTerminalRequest {
    @NotBlank(message = "터미널 ID는 필수입니다")
    private String terminalId;

    @NotBlank(message = "봇 UID는 필수입니다")
    private String botUid;

    @NotBlank(message = "POS UID는 필수입니다")
    private String posUid;

    public ReleaseTerminalRequest() {
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