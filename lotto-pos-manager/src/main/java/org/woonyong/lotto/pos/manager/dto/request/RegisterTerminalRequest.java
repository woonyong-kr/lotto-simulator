package org.woonyong.lotto.pos.manager.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterTerminalRequest {
    @NotBlank(message = "터미널 ID는 필수입니다")
    private String terminalId;

    public RegisterTerminalRequest() {
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(final String terminalId) {
        this.terminalId = terminalId;
    }
}