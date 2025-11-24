package org.woonyong.lotto.bot.domain;

public enum HealthStatus {
    GREEN,
    ORANGE,
    RED;

    public static HealthStatus fromFailureCount(final int failureCount, final int threshold) {
        if (failureCount == 0) {
            return GREEN;
        }
        if (failureCount < threshold) {
            return ORANGE;
        }
        return RED;
    }
}
