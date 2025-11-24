package org.woonyong.lotto.central.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.dto.response.RoundDetailResponse;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.entity.WinningNumber;
import org.woonyong.lotto.central.repository.RoundRepository;
import org.woonyong.lotto.central.repository.TicketRepository;
import org.woonyong.lotto.central.repository.WinningNumberRepository;

@Service
@Transactional(readOnly = true)
public class RoundStatisticsService {
  private static final String ERROR_NO_CURRENT_ROUND = "진행 중인 회차가 없습니다.";
  private static final String NUMBER_DELIMITER = ",";

  private final RoundRepository roundRepository;
  private final TicketRepository ticketRepository;
  private final WinningNumberRepository winningNumberRepository;

  public RoundStatisticsService(
      final RoundRepository roundRepository,
      final TicketRepository ticketRepository,
      final WinningNumberRepository winningNumberRepository) {
    this.roundRepository = roundRepository;
    this.ticketRepository = ticketRepository;
    this.winningNumberRepository = winningNumberRepository;
  }

  public List<RoundDetailResponse> getRecentRounds() {
    List<Round> rounds = roundRepository.findTop5ByOrderByIdDesc();
    return rounds.stream().map(this::toRoundDetailResponse).toList();
  }

  public RoundDetailResponse getCurrentRound() {
    Round round =
        roundRepository
            .findTopByOrderByIdDesc()
            .orElseThrow(() -> new IllegalStateException(ERROR_NO_CURRENT_ROUND));
    return toRoundDetailResponse(round);
  }

  private RoundDetailResponse toRoundDetailResponse(final Round round) {
    Long roundId = round.getId();
    long totalTickets = ticketRepository.countByRoundId(roundId);
    long remainingSeconds = round.getRemainingSeconds(java.time.LocalDateTime.now());

    return RoundDetailResponse.of(
        roundId,
        round.getRoundNumber(),
        round.getStatus(),
        getWinningNumbers(roundId),
        getBonusNumber(roundId),
        totalTickets,
        countWinners(roundId, 1),
        countWinners(roundId, 2),
        countWinners(roundId, 3),
        countWinners(roundId, 4),
        countWinners(roundId, 5),
        remainingSeconds,
        round.getEndTime());
  }

  private List<Integer> getWinningNumbers(final Long roundId) {
    return winningNumberRepository
        .findByRoundId(roundId)
        .map(this::parseWinningNumbers)
        .orElse(Collections.emptyList());
  }

  private List<Integer> parseWinningNumbers(final WinningNumber winningNumber) {
    return Arrays.stream(winningNumber.getNumbers().split(NUMBER_DELIMITER))
        .map(Integer::parseInt)
        .toList();
  }

  private Integer getBonusNumber(final Long roundId) {
    return winningNumberRepository
        .findByRoundId(roundId)
        .map(WinningNumber::getBonusNumber)
        .orElse(null);
  }

  private long countWinners(final Long roundId, final int rank) {
    return ticketRepository.countByRoundIdAndWinningRank(roundId, rank);
  }
}
