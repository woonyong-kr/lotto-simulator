package org.woonyong.lotto.central.entity;

import jakarta.persistence.*;
import org.woonyong.lotto.core.domain.RoundStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rounds")
public class Round {
    private static final String ERROR_DRAWING_NOT_COMPLETED = "집계가 완료되지 않아 시간을 변경할 수 없습니다";
    private static final String ERROR_INVALID_STATUS_FOR_CLOSE = "OPEN 상태에서만 CLOSED로 전환 가능합니다";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer roundNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoundStatus status;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean drawingCompleted;

    protected Round() {
    }

    public static Round create(final Integer roundNumber, final LocalDateTime startTime,
                               final LocalDateTime endTime) {
        Round round = new Round();
        round.roundNumber = roundNumber;
        round.status = RoundStatus.OPEN;
        round.startTime = startTime;
        round.endTime = endTime;
        round.drawingCompleted = false;
        return round;
    }

    public void close() {
        validateCanClose();
        this.status = RoundStatus.CLOSED;
    }

    public void open() {
        this.status = RoundStatus.OPEN;
        this.drawingCompleted = false;
    }

    public void completeDrawing() {
        this.drawingCompleted = true;
    }

    public void updateEndTime(final LocalDateTime newEndTime) {
        if (status == RoundStatus.CLOSED && !drawingCompleted) {
            throw new IllegalStateException(ERROR_DRAWING_NOT_COMPLETED);
        }
        this.endTime = newEndTime;
    }

    public boolean isTimeExpired(final LocalDateTime now) {
        return now.isAfter(endTime) || now.isEqual(endTime);
    }

    public boolean canTransitionToOpen() {
        return status == RoundStatus.CLOSED && drawingCompleted;
    }

    public long getRemainingSeconds(final LocalDateTime now) {
        if (now.isAfter(endTime)) {
            return 0;
        }
        return java.time.Duration.between(now, endTime).getSeconds();
    }

    public Long getId() {
        return id;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public RoundStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Boolean getDrawingCompleted() {
        return drawingCompleted;
    }

    private void validateCanClose() {
        if (status != RoundStatus.OPEN) {
            throw new IllegalStateException(ERROR_INVALID_STATUS_FOR_CLOSE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Round round)) {
            return false;
        }
        return Objects.equals(id, round.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}