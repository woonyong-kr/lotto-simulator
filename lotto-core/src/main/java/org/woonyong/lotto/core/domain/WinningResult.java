package org.woonyong.lotto.core.domain;

import java.util.Objects;

public final class WinningResult {
    private final WinningRank rank;
    private final int prizeMoney;

    public static WinningResult of(final WinningRank rank) {
        return new WinningResult(rank);
    }

    private WinningResult(final WinningRank rank) {
        this.rank = rank;
        this.prizeMoney = rank.getPrizeMoney();
    }

    public static WinningResult miss() {
        return new WinningResult(WinningRank.MISS);
    }

    public WinningRank getRank() {
        return rank;
    }

    public int getPrizeMoney() {
        return prizeMoney;
    }

    public boolean isWinner() {
        return rank != WinningRank.MISS;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WinningResult that)) {
            return false;
        }
        return prizeMoney == that.prizeMoney && rank == that.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, prizeMoney);
    }
}