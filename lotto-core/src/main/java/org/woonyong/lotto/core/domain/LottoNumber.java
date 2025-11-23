package org.woonyong.lotto.core.domain;

import org.woonyong.lotto.core.constant.LottoConstants;
import org.woonyong.lotto.core.exception.InvalidLottoNumberException;

import java.util.Objects;

public final class LottoNumber implements Comparable<LottoNumber> {
    private final int value;

    public static LottoNumber of(final int value) {
        return new LottoNumber(value);
    }

    private LottoNumber(final int value) {
        validateRange(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(final LottoNumber other) {
        return Integer.compare(this.value, other.value);
    }

    private void validateRange(final int value) {
        if (value < LottoConstants.MIN_NUMBER || value > LottoConstants.MAX_NUMBER) {
            throw new InvalidLottoNumberException(value);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LottoNumber)) {
            return false;
        }
        LottoNumber that = (LottoNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
