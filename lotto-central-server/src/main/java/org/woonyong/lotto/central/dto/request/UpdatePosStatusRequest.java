package org.woonyong.lotto.central.dto.request;

public class UpdatePosStatusRequest {
    private boolean active;

    public UpdatePosStatusRequest() {
    }

    public UpdatePosStatusRequest(final boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
