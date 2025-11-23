package org.woonyong.lotto.core.domain;

import java.util.Arrays;

public enum WinningRank {
    FIRST(6, false, 2_000_000_000, "1등"),
    SECOND(5, true, 30_000_000, "2등"),
    THIRD(5, false, 1_500_000, "3등"),
    FOURTH(4, false, 50_000, "4등"),
    FIFTH(3, false, 5_000, "5등"),
    MISS(0, false, 0, "낙첨");

    private final int matchCount;
    private final boolean matchBonus;
    private final int prizeMoney;
    private final String description;

    WinningRank(final int matchCount, final boolean matchBonus,
                final int prizeMoney, final String description) {
        this.matchCount = matchCount;
        this.matchBonus = matchBonus;
        this.prizeMoney = prizeMoney;
        this.description = description;
    }

    public static WinningRank of(final int matchCount, final boolean matchBonus) {
        return Arrays.stream(values())
                .filter(rank -> rank.matches(matchCount, matchBonus))
                .findFirst()
                .orElse(MISS);
    }

    public int getMatchCount() {
        return matchCount;
    }

    public int getPrizeMoney() {
        return prizeMoney;
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