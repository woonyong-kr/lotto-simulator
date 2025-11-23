package org.woonyong.lotto.core.domain;

import org.woonyong.lotto.core.exception.DuplicateBonusNumberException;

import java.util.Objects;

public final class WinningNumbers {
    private final LottoNumbers winningNumbers;
    private final LottoNumber bonusNumber;

    public static WinningNumbers of(final LottoNumbers winningNumbers, final LottoNumber bonusNumber) {
        return new WinningNumbers(winningNumbers, bonusNumber);
    }

    private WinningNumbers(final LottoNumbers winningNumbers, final LottoNumber bonusNumber) {
        validateBonusNumber(winningNumbers, bonusNumber);
        this.winningNumbers = winningNumbers;
        this.bonusNumber = bonusNumber;
    }

    public WinningRank match(final LottoNumbers userNumbers) {
        int matchCount = winningNumbers.countMatches(userNumbers);
        boolean matchBonus = userNumbers.contains(bonusNumber);
        return WinningRank.of(matchCount, matchBonus);
    }

    public LottoNumbers getWinningNumbers() {
        return winningNumbers;
    }

    public LottoNumber getBonusNumber() {
        return bonusNumber;
    }

    private void validateBonusNumber(final LottoNumbers winningNumbers, final LottoNumber bonusNumber) {
        if (winningNumbers.contains(bonusNumber)) {
            throw new DuplicateBonusNumberException();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WinningNumbers that)) {
            return false;
        }
        return Objects.equals(winningNumbers, that.winningNumbers)
                && Objects.equals(bonusNumber, that.bonusNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winningNumbers, bonusNumber);
    }
}