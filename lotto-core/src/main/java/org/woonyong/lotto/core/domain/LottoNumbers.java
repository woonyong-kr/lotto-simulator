package org.woonyong.lotto.core.domain;

import org.woonyong.lotto.core.constant.LottoConstants;
import org.woonyong.lotto.core.exception.InvalidLottoNumbersCountException;
import org.woonyong.lotto.core.exception.DuplicateLottoNumberException;

import java.util.*;
import java.util.stream.Collectors;

public final class LottoNumbers {
    private final List<LottoNumber> numbers;

    public static LottoNumbers of(final List<LottoNumber> numbers) {
        return new LottoNumbers(numbers);
    }

    public static LottoNumbers from(final List<Integer> values) {
        List<LottoNumber> numbers = values.stream()
                .map(LottoNumber::of)
                .collect(Collectors.toList());
        return new LottoNumbers(numbers);
    }

    private LottoNumbers(final List<LottoNumber> numbers) {
        validateCount(numbers);
        validateDuplicate(numbers);
        this.numbers = sortAndMakeUnmodifiable(numbers);
    }

    public List<LottoNumber> getNumbers() {
        return numbers;
    }

    public boolean contains(final LottoNumber number) {
        return numbers.contains(number);
    }

    public int countMatches(final LottoNumbers other) {
        return (int) this.numbers.stream()
                .filter(other.numbers::contains)
                .count();
    }

    private void validateCount(final List<LottoNumber> numbers) {
        if (numbers.size() != LottoConstants.NUMBERS_COUNT) {
            throw new InvalidLottoNumbersCountException(numbers.size());
        }
    }

    private void validateDuplicate(final List<LottoNumber> numbers) {
        Set<LottoNumber> uniqueNumbers = new HashSet<>(numbers);
        if (uniqueNumbers.size() != numbers.size()) {
            throw new DuplicateLottoNumberException();
        }
    }

    private List<LottoNumber> sortAndMakeUnmodifiable(final List<LottoNumber> numbers) {
        return numbers.stream()
                .sorted()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LottoNumbers)) {
            return false;
        }
        LottoNumbers that = (LottoNumbers) o;
        return Objects.equals(numbers, that.numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numbers);
    }

    @Override
    public String toString() {
        return numbers.toString();
    }
}
