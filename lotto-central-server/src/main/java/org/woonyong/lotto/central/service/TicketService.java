package org.woonyong.lotto.central.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Pos;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.central.entity.WinningNumber;
import org.woonyong.lotto.central.repository.PosRepository;
import org.woonyong.lotto.central.repository.RoundRepository;
import org.woonyong.lotto.central.repository.TicketRepository;
import org.woonyong.lotto.central.repository.WinningNumberRepository;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.domain.RoundStatus;
import org.woonyong.lotto.core.domain.WinningNumbers;

@Service
@Transactional(readOnly = true)
public class TicketService {
  private static final String ERROR_ROUND_NOT_FOUND = "회차를 찾을 수 없습니다";
  private static final String ERROR_ROUND_NOT_OPEN = "현재 회차가 구매 가능 상태가 아닙니다";
  private static final String ERROR_NO_WINNING_NUMBER = "당첨 번호가 없습니다";
  private static final String ERROR_TICKET_NOT_FOUND = "티켓을 찾을 수 없습니다";
  private static final String ERROR_POS_NOT_FOUND = "존재하지 않는 POS입니다: ";

  private final TicketRepository ticketRepository;
  private final WinningNumberRepository winningNumberRepository;
  private final RoundRepository roundRepository;
  private final PosRepository posRepository;

  public TicketService(
      final TicketRepository ticketRepository,
      final WinningNumberRepository winningNumberRepository,
      final RoundRepository roundRepository,
      final PosRepository posRepository) {
    this.ticketRepository = ticketRepository;
    this.winningNumberRepository = winningNumberRepository;
    this.roundRepository = roundRepository;
    this.posRepository = posRepository;
  }

  @Transactional
  public Ticket purchaseManualTicket(
      final Long roundId, final LottoNumbers numbers, final String posUid) {
    Round round = findRoundByIdForUpdate(roundId);
    validateRoundIsOpen(round);

    Pos pos = findPosByUidForUpdate(posUid);

    Ticket ticket = Ticket.createManual(roundId, numbers, posUid);
    ticketRepository.save(ticket);

    pos.addSales(1);

    return ticket;
  }

  @Transactional
  public Ticket purchaseAutoTicket(final Long roundId, final String posUid) {
    Round round = findRoundByIdForUpdate(roundId);
    validateRoundIsOpen(round);

    Pos pos = findPosByUidForUpdate(posUid);

    LottoNumbers autoNumbers = LottoNumbers.generateRandom();
    Ticket ticket = Ticket.createAuto(roundId, autoNumbers, posUid);
    ticketRepository.save(ticket);

    pos.addSales(1);

    return ticket;
  }

  @Transactional
  public void checkWinning(final Long roundId) {
    WinningNumber winningNumber = findWinningNumberByRoundId(roundId);
    WinningNumbers winningNumbers = winningNumber.toWinningNumbers();

    List<Ticket> tickets = findTicketsByRoundId(roundId);
    checkAllTickets(tickets, winningNumbers);
    updatePosWinnings(tickets);
  }

  public Ticket findByTicketNumber(final String ticketNumber) {
    return ticketRepository
        .findByTicketNumber(ticketNumber)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_TICKET_NOT_FOUND));
  }

  private Round findRoundByIdForUpdate(final Long roundId) {
    return roundRepository
        .findByIdForUpdate(roundId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_ROUND_NOT_FOUND));
  }

  private Pos findPosByUidForUpdate(final String posUid) {
    return posRepository
        .findByPosUidForUpdate(posUid)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_POS_NOT_FOUND + posUid));
  }

  private WinningNumber findWinningNumberByRoundId(final Long roundId) {
    return winningNumberRepository
        .findByRoundId(roundId)
        .orElseThrow(() -> new IllegalStateException(ERROR_NO_WINNING_NUMBER));
  }

  private List<Ticket> findTicketsByRoundId(final Long roundId) {
    return ticketRepository.findByRoundId(roundId);
  }

  private void checkAllTickets(final List<Ticket> tickets, final WinningNumbers winningNumbers) {
    for (Ticket ticket : tickets) {
      ticket.checkWinning(winningNumbers);
    }
    ticketRepository.saveAll(tickets);
  }

  private void updatePosWinnings(final List<Ticket> tickets) {
    for (Ticket ticket : tickets) {
      updateSinglePosWinning(ticket);
    }
  }

  private void updateSinglePosWinning(final Ticket ticket) {
    if (!ticket.isWinner()) {
      return;
    }
    if (ticket.getPosUid() == null) {
      return;
    }

    Pos pos = findPosByUidForUpdate(ticket.getPosUid());
    pos.addWinnings(ticket.getWinningAmount());
  }

  private void validateRoundIsOpen(final Round round) {
    if (round.getStatus() != RoundStatus.OPEN) {
      throw new IllegalStateException(ERROR_ROUND_NOT_OPEN);
    }
  }
}
