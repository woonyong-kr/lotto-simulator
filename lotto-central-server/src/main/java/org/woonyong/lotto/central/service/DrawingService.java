package org.woonyong.lotto.central.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.entity.WinningNumber;
import org.woonyong.lotto.central.repository.RoundRepository;
import org.woonyong.lotto.central.repository.WinningNumberRepository;
import org.woonyong.lotto.core.domain.LottoNumber;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.domain.WinningNumbers;

@Service
public class DrawingService {
  private static final String ERROR_ROUND_NOT_FOUND = "회차를 찾을 수 없습니다";

  private final RoundRepository roundRepository;
  private final WinningNumberRepository winningNumberRepository;
  private final TicketService ticketService;

  public DrawingService(
      final RoundRepository roundRepository,
      final WinningNumberRepository winningNumberRepository,
      final TicketService ticketService) {
    this.roundRepository = roundRepository;
    this.winningNumberRepository = winningNumberRepository;
    this.ticketService = ticketService;
  }

  @Async
  @Transactional
  public void startDrawingAsync(final Long roundId) {
    WinningNumbers winningNumbers = generateWinningNumbers();
    WinningNumber winningNumber = WinningNumber.create(roundId, winningNumbers);
    winningNumberRepository.save(winningNumber);

    ticketService.checkWinning(roundId);

    Round round =
        roundRepository
            .findById(roundId)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_ROUND_NOT_FOUND));
    round.completeDrawing();
    roundRepository.save(round);
  }

  private WinningNumbers generateWinningNumbers() {
    LottoNumbers mainNumbers = LottoNumbers.generateRandom();
    LottoNumber bonusNumber = LottoNumber.generateRandom(mainNumbers);
    return WinningNumbers.of(mainNumbers, bonusNumber);
  }
}
