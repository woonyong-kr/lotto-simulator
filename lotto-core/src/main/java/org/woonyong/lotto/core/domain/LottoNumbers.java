package org.woonyong.lotto.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.woonyong.lotto.core.constant.LottoConstants;
import org.woonyong.lotto.core.exception.DuplicateLottoNumberException;
import org.woonyong.lotto.core.exception.InvalidLottoNumbersCountException;
import org.woonyong.lotto.core.util.LottoNumberGenerator;

public final class LottoNumbers {
  private final List<LottoNumber> numbers;

  public static LottoNumbers from(final List<Integer> values) {
    List<LottoNumber> lottoNumbers = values.stream().map(LottoNumber::of).toList();
    return new LottoNumbers(lottoNumbers);
  }

  public static LottoNumbers generateRandom() {
    return from(LottoNumberGenerator.generateRandomNumbers());
  }

  public static LottoNumbers generateRandom(final int excludeNumber) {
    return from(LottoNumberGenerator.generateRandomNumbers(excludeNumber));
  }

  public static LottoNumbers generateRandom(final List<Integer> excludeNumbers) {
    return from(LottoNumberGenerator.generateRandomNumbers(excludeNumbers));
  }

  public static LottoNumbers generateRandom(LottoNumber excludeNumber) {
    return from(LottoNumberGenerator.generateRandomNumbers(excludeNumber.getNumber()));
  }

  public static LottoNumbers generateRandom(LottoNumbers excludeNumbers) {
    List<Integer> excludeValues =
        excludeNumbers.getNumbers().stream().map(LottoNumber::getNumber).toList();
    return from(LottoNumberGenerator.generateRandomNumbers(excludeValues));
  }

  private LottoNumbers(final List<LottoNumber> numbers) {
    validateCount(numbers);
    validateDuplicate(numbers);
    this.numbers = new ArrayList<>(numbers);
  }

  public boolean contains(final LottoNumber number) {
    return numbers.contains(number);
  }

  public int countMatches(final LottoNumbers other) {
    return (int) numbers.stream().filter(other::contains).count();
  }

  public List<LottoNumber> getNumbers() {
    return new ArrayList<>(numbers);
  }

  private void validateCount(final List<LottoNumber> numbers) {
    if (numbers.size() != LottoConstants.LOTTO_NUMBERS_COUNT) {
      throw new InvalidLottoNumbersCountException(numbers.size());
    }
  }

  private void validateDuplicate(final List<LottoNumber> numbers) {
    Set<LottoNumber> uniqueNumbers = new HashSet<>(numbers);
    if (uniqueNumbers.size() != numbers.size()) {
      throw new DuplicateLottoNumberException();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LottoNumbers that)) {
      return false;
    }
    return Objects.equals(numbers, that.numbers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numbers);
  }
}
