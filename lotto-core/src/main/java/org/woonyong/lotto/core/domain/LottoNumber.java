package org.woonyong.lotto.core.domain;

import java.util.List;
import java.util.Objects;
import org.woonyong.lotto.core.constant.LottoConstants;
import org.woonyong.lotto.core.exception.InvalidLottoNumberException;
import org.woonyong.lotto.core.util.LottoNumberGenerator;

public final class LottoNumber {
  private final int number;

  public static LottoNumber of(final int value) {
    return new LottoNumber(value);
  }

  public static LottoNumber generateRandom() {
    return of(LottoNumberGenerator.generateRandomNumber());
  }

  public static LottoNumber generateRandom(final int excludeNumber) {
    return of(LottoNumberGenerator.generateRandomNumber(excludeNumber));
  }

  public static LottoNumber generateRandom(final List<Integer> excludeNumbers) {
    return of(LottoNumberGenerator.generateRandomNumber(excludeNumbers));
  }

  public static LottoNumber generateRandom(LottoNumber excludeNumber) {
    return of(LottoNumberGenerator.generateRandomNumber(excludeNumber.getNumber()));
  }

  public static LottoNumber generateRandom(LottoNumbers excludeNumbers) {
    List<Integer> excludeValues =
        excludeNumbers.getNumbers().stream().map(LottoNumber::getNumber).toList();
    return of(LottoNumberGenerator.generateRandomNumber(excludeValues));
  }

  private LottoNumber(final int value) {
    validate(value);
    this.number = value;
  }

  public int getNumber() {
    return number;
  }

  private void validate(final int value) {
    if (value < LottoConstants.MIN_NUMBER || value > LottoConstants.MAX_NUMBER) {
      throw new InvalidLottoNumberException(value);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LottoNumber that)) {
      return false;
    }
    return number == that.number;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }
}
