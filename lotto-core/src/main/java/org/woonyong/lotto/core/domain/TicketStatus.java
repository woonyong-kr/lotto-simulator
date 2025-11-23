package org.woonyong.lotto.core.domain;

public enum TicketStatus {
    ISSUED("발행됨"),
    CANCELLED("취소됨"),
    WINNING_CLAIMED("당첨금수령완료");

    private final String description;

    TicketStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isIssued() {
        return this == ISSUED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean canBeCancelled() {
        return this == ISSUED;
    }
}