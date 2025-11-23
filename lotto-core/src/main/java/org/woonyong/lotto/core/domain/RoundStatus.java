package org.woonyong.lotto.core.domain;

public enum RoundStatus {
    OPEN("판매중"),
    CLOSED("판매마감");

    private final String description;

    RoundStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOpen() {
        return this == OPEN;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}