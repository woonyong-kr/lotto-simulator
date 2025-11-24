package org.woonyong.lotto.core.domain;

import java.util.Arrays;

public enum WinningRank {
    FIRST(1, 6, "1등"),
    SECOND(2, 5, "2등"),
    THIRD(3, 5, "3등"),
    FOURTH(4, 4, "4등"),
    FIFTH(5, 3, "5등"),
    MISS(0, 0, "낙첨");

    private final int rank;
    private final int matchCount;
    private final String description;

    WinningRank(final int rank, final int matchCount, final String description) {
        this.rank = rank;
        this.matchCount = matchCount;
        this.description = description;
    }

    public static WinningRank of(final int matchCount, final boolean matchBonus) {
        return Arrays.stream(values())
                .filter(r -> r.matches(matchCount, matchBonus))
                .findFirst()
                .orElse(MISS);
    }

    public int getRank() {
        return rank;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public String getDescription() {
        return description;
    }

    private boolean matches(final int matchCount, final boolean matchBonus) {
        if (this == SECOND) {
            return this.matchCount == matchCount && matchBonus;
        }
        if (this == MISS) {
            return matchCount < FIFTH.matchCount;
        }
        return this.matchCount == matchCount;
    }
}