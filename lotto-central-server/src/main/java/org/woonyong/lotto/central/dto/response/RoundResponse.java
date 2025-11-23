package org.woonyong.lotto.central.dto.response;

import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.core.domain.RoundStatus;

import java.time.LocalDateTime;

public class RoundResponse {
    private Long id;
    private Integer roundNumber;
    private RoundStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean drawingCompleted;
    private Long remainingSeconds;

    protected RoundResponse() {
    }

    private RoundResponse(final Long id,
                          final Integer roundNumber,
                          final RoundStatus status,
                          final LocalDateTime startTime,
                          final LocalDateTime endTime,
                          final Boolean drawingCompleted,
                          final Long remainingSeconds) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.drawingCompleted = drawingCompleted;
        this.remainingSeconds = remainingSeconds;
    }

    public static RoundResponse from(final Round round) {
        LocalDateTime now = LocalDateTime.now();
        long remainingSeconds = round.getRemainingSeconds(now);

        return new RoundResponse(
                round.getId(),
                round.getRoundNumber(),
                round.getStatus(),
                round.getStartTime(),
                round.getEndTime(),
                round.getDrawingCompleted(),
                remainingSeconds
        );
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

    public Long getRemainingSeconds() {
        return remainingSeconds;
    }
}