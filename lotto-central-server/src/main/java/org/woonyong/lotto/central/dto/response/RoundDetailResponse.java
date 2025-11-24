package org.woonyong.lotto.central.dto.response;

import static org.woonyong.lotto.core.constant.LottoConstants.*;

import java.time.LocalDateTime;
import java.util.List;
import org.woonyong.lotto.core.domain.RoundStatus;

public record RoundDetailResponse(
    Long id,
    Integer roundNumber,
    RoundStatus status,
    List<Integer> winningNumbers,
    Integer bonusNumber,
    long totalTickets,
    long totalSales,
    long firstWinners,
    long secondWinners,
    long thirdWinners,
    long fourthWinners,
    long fifthWinners,
    long firstPrize,
    long secondPrize,
    long thirdPrize,
    long totalPrize,
    long remainingSeconds,
    String endTimeFormatted) {
  public static RoundDetailResponse of(
      final Long id,
      final Integer roundNumber,
      final RoundStatus status,
      final List<Integer> winningNumbers,
      final Integer bonusNumber,
      final long totalTickets,
      final long firstWinners,
      final long secondWinners,
      final long thirdWinners,
      final long fourthWinners,
      final long fifthWinners,
      final long remainingSeconds,
      final LocalDateTime endTime) {
    long totalSales = totalTickets * TICKET_PRICE;
    long totalPrize =
        calculateTotalPrize(firstWinners, secondWinners, thirdWinners, fourthWinners, fifthWinners);
    String endTimeFormatted = formatEndTime(endTime);

    return new RoundDetailResponse(
        id,
        roundNumber,
        status,
        winningNumbers,
        bonusNumber,
        totalTickets,
        totalSales,
        firstWinners,
        secondWinners,
        thirdWinners,
        fourthWinners,
        fifthWinners,
        UNDETERMINED_PRIZE,
        UNDETERMINED_PRIZE,
        UNDETERMINED_PRIZE,
        totalPrize,
        remainingSeconds,
        endTimeFormatted);
  }

  private static long calculateTotalPrize(
      final long first, final long second, final long third, final long fourth, final long fifth) {
    return first * 2000000000L
        + second * 50000000L
        + third * 1500000L
        + fourth * 50000L
        + fifth * 5000L;
  }

  private static String formatEndTime(final LocalDateTime endTime) {
    if (endTime == null) {
      return "";
    }
    return String.format(
        "%04d-%02d-%02d %02d:%02d 추첨",
        endTime.getYear(),
        endTime.getMonthValue(),
        endTime.getDayOfMonth(),
        endTime.getHour(),
        endTime.getMinute());
  }
}
