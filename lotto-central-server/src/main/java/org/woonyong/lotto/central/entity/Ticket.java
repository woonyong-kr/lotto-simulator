package org.woonyong.lotto.central.entity;

import jakarta.persistence.*;
import org.woonyong.lotto.core.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "tickets")
public class Ticket {
    private static final String ERROR_CANNOT_CANCEL = "취소할 수 없는 티켓입니다.";
    private static final String ERROR_CANNOT_CHECK_WINNING = "발행된 티켓만 당첨 확인이 가능합니다.";
    private static final String NUMBER_DELIMITER = ",";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String ticketNumber;

    @Column(nullable = false)
    private Long roundId;

    @Column(nullable = false, length = 50)
    private String numbers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    private Integer winningRank;

    private Integer winningAmount;

    @Column(nullable = false)
    private LocalDateTime purchaseTime;

    protected Ticket() {
    }

    public static Ticket createManual(final Long roundId, final LottoNumbers lottoNumbers) {
        return create(roundId, lottoNumbers, TicketType.MANUAL);
    }

    public static Ticket createAuto(final Long roundId, final LottoNumbers lottoNumbers) {
        return create(roundId, lottoNumbers, TicketType.AUTO);
    }

    public void checkWinning(final WinningNumbers winningNumbers) {
        validateCanCheckWinning();
        LottoNumbers lottoNumbers = convertToLottoNumbers();
        WinningRank rank = winningNumbers.match(lottoNumbers);
        applyWinningResult(rank);
    }

    public void cancel() {
        validateCanCancel();
        this.status = TicketStatus.CANCELLED;
    }

    public boolean isWinner() {
        return winningRank != null && winningRank > 0;
    }

    public Long getId() {
        return id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Long getRoundId() {
        return roundId;
    }

    public String getNumbers() {
        return numbers;
    }

    public TicketType getType() {
        return type;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Integer getWinningRank() {
        return winningRank;
    }

    public Integer getWinningAmount() {
        return winningAmount;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    private static Ticket create(final Long roundId, final LottoNumbers lottoNumbers, final TicketType type) {
        Ticket ticket = new Ticket();
        ticket.ticketNumber = TicketNumber.generate().getValue();
        ticket.roundId = roundId;
        ticket.numbers = convertToString(lottoNumbers);
        ticket.type = type;
        ticket.status = TicketStatus.ISSUED;
        ticket.purchaseTime = LocalDateTime.now();
        return ticket;
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

    private void applyWinningResult(final WinningRank rank) {
        this.winningRank = rank.getMatchCount();
        this.winningAmount = rank.getPrizeMoney();
    }

    private void validateCanCheckWinning() {
        if (status != TicketStatus.ISSUED) {
            throw new IllegalStateException(ERROR_CANNOT_CHECK_WINNING);
        }
    }

    private void validateCanCancel() {
        if (!status.canBeCancelled()) {
            throw new IllegalStateException(ERROR_CANNOT_CANCEL);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket ticket)) {
            return false;
        }
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}