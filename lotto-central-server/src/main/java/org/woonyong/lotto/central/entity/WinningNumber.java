package org.woonyong.lotto.central.entity;

import jakarta.persistence.*;
import org.woonyong.lotto.core.domain.LottoNumber;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.domain.WinningNumbers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class WinningNumber {
    private static final String NUMBER_DELIMITER = ",";

    @Id
    private Long roundId;

    @Column(nullable = false, length = 50)
    private String numbers;

    @Column(nullable = false)
    private Integer bonusNumber;

    @Column(nullable = false)
    private LocalDateTime drawnAt;

    protected WinningNumber() {
    }

    public static WinningNumber create(final Long roundId, final WinningNumbers winningNumbers) {
        WinningNumber winningNumber = new WinningNumber();
        winningNumber.roundId = roundId;
        winningNumber.numbers = convertToString(winningNumbers.getWinningNumbers());
        winningNumber.bonusNumber = winningNumbers.getBonusNumber().getNumber();
        winningNumber.drawnAt = LocalDateTime.now();
        return winningNumber;
    }

    public WinningNumbers toWinningNumbers() {
        LottoNumbers lottoNumbers = convertToLottoNumbers();
        LottoNumber bonus = LottoNumber.of(bonusNumber);
        return WinningNumbers.of(lottoNumbers, bonus);
    }

    public Long getRoundId() {
        return roundId;
    }

    public String getNumbers() {
        return numbers;
    }

    public Integer getBonusNumber() {
        return bonusNumber;
    }

    public LocalDateTime getDrawnAt() {
        return drawnAt;
    }

    private static String convertToString(final LottoNumbers lottoNumbers) {
        return lottoNumbers.getNumbers().stream()
                .map(LottoNumber::getNumber)
                .map(String::valueOf)
                .collect(Collectors.joining(NUMBER_DELIMITER));
    }

    private LottoNumbers convertToLottoNumbers() {
        List<Integer> numberList = Arrays.stream(numbers.split(NUMBER_DELIMITER))
                .map(Integer::parseInt)
                .toList();
        return LottoNumbers.from(numberList);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WinningNumber that)) {
            return false;
        }
        return Objects.equals(roundId, that.roundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundId);
    }
}