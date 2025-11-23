package org.woonyong.lotto.central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.central.entity.WinningNumber;
import org.woonyong.lotto.central.repository.TicketRepository;
import org.woonyong.lotto.central.repository.WinningNumberRepository;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.domain.RoundStatus;
import org.woonyong.lotto.core.domain.WinningNumbers;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TicketService {
    private static final String ERROR_NO_CURRENT_ROUND = "진행 중인 회차가 없습니다";
    private static final String ERROR_ROUND_NOT_OPEN = "현재 회차가 구매 가능 상태가 아닙니다";
    private static final String ERROR_ROUND_MISMATCH = "요청한 회차와 현재 회차가 일치하지 않습니다";
    private static final String ERROR_NO_WINNING_NUMBER = "당첨 번호가 없습니다";

    private final TicketRepository ticketRepository;
    private final WinningNumberRepository winningNumberRepository;
    private final RoundService roundService;

    public TicketService(final TicketRepository ticketRepository,
                         final WinningNumberRepository winningNumberRepository,
                         final RoundService roundService) {
        this.ticketRepository = ticketRepository;
        this.winningNumberRepository = winningNumberRepository;
        this.roundService = roundService;
    }

    @Transactional
    public Ticket purchaseManualTicket(final Long roundId, final LottoNumbers numbers) {
        Round round = findCurrentRound();
        validateRoundIsOpen(round);
        validateRoundId(round, roundId);

        Ticket ticket = Ticket.createManual(roundId, numbers);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket purchaseAutoTicket(final Long roundId) {
        Round round = findCurrentRound();
        validateRoundIsOpen(round);
        validateRoundId(round, roundId);

        LottoNumbers autoNumbers = LottoNumbers.generateRandom();
        Ticket ticket = Ticket.createAuto(roundId, autoNumbers);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void checkWinning(final Long roundId) {
        WinningNumber winningNumber = findWinningNumberByRoundId(roundId);
        WinningNumbers winningNumbers = winningNumber.toWinningNumbers();

        List<Ticket> tickets = findTicketsByRoundId(roundId);
        checkAllTickets(tickets, winningNumbers);
    }

    private Round findCurrentRound() {
        return roundService.getCurrentRound()
                .orElseThrow(() -> new IllegalStateException(ERROR_NO_CURRENT_ROUND));
    }

    private WinningNumber findWinningNumberByRoundId(final Long roundId) {
        return winningNumberRepository.findByRoundId(roundId)
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

    private void validateRoundIsOpen(final Round round) {
        if (round.getStatus() != RoundStatus.OPEN) {
            throw new IllegalStateException(ERROR_ROUND_NOT_OPEN);
        }
    }

    private void validateRoundId(final Round round, final Long expectedRoundId) {
        if (!round.getId().equals(expectedRoundId)) {
            throw new IllegalArgumentException(ERROR_ROUND_MISMATCH);
        }
    }
}