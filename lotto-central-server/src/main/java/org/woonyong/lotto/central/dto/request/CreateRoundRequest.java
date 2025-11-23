package org.woonyong.lotto.central.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class CreateRoundRequest {
    @NotNull(message = "회차 번호는 필수입니다")
    @Positive(message = "회차 번호는 양수여야 합니다")
    private Integer roundNumber;

    @NotNull(message = "시작 시간은 필수입니다")
    @Future(message = "시작 시간은 미래여야 합니다")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간은 필수입니다")
    @Future(message = "종료 시간은 미래여야 합니다")
    private LocalDateTime endTime;

    protected CreateRoundRequest() {
    }

    public CreateRoundRequest(final Integer roundNumber, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.roundNumber = roundNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}