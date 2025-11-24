package org.woonyong.lotto.central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.central.entity.WinningNumber;
import org.woonyong.lotto.central.repository.RoundRepository;
import org.woonyong.lotto.central.repository.TicketRepository;
import org.woonyong.lotto.central.repository.WinningNumberRepository;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.domain.RoundStatus;
import org.woonyong.lotto.core.domain.WinningNumbers;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TicketService {
    private static final String ERROR_ROUND_NOT_FOUND = "회차를 찾을 수 없습니다";
    private static final String ERROR_ROUND_NOT_OPEN = "현재 회차가 구매 가능 상태가 아닙니다";
    private static final String ERROR_NO_WINNING_NUMBER = "당첨 번호가 없습니다";
    private static final String ERROR_TICKET_NOT_FOUND = "티켓을 찾을 수 없습니다";

    private final TicketRepository ticketRepository;
    private final WinningNumberRepository winningNumberRepository;
    private final RoundRepository roundRepository;

    public TicketService(final TicketRepository ticketRepository,
                         final WinningNumberRepository winningNumberRepository,
                         final RoundRepository roundRepository) {
        this.ticketRepository = ticketRepository;
        this.winningNumberRepository = winningNumberRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public Ticket purchaseManualTicket(final Long roundId, final LottoNumbers numbers) {
        Round round = findRoundByIdForUpdate(roundId);


        validateRoundIsOpen(round);

        Ticket ticket = Ticket.createManual(roundId, numbers);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket purchaseAutoTicket(final Long roundId) {
        Round round = findRoundByIdForUpdate(roundId);


        validateRoundIsOpen(round);

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

    public Ticket findByTicketNumber(final String ticketNumber) {
        return ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_TICKET_NOT_FOUND));
    }

    private Round findRoundByIdForUpdate(final Long roundId) {
        return roundRepository.findByIdForUpdate(roundId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_ROUND_NOT_FOUND));
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
}