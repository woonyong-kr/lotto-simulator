package org.woonyong.lotto.central.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.central.dto.response.RoundResponse;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.service.RoundService;
import org.woonyong.lotto.core.dto.ApiResponse;

@RestController
@RequestMapping("/api/rounds")
public class RoundController {
    private static final String ERROR_NO_CURRENT_ROUND = "진행 중인 회차가 없습니다";

    private final RoundService roundService;

    public RoundController(final RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/current")
    public ApiResponse<RoundResponse> getCurrentRound() {
        Round round = roundService.getCurrentRound()
                .orElseThrow(() -> new IllegalStateException(ERROR_NO_CURRENT_ROUND));
        return ApiResponse.success(RoundResponse.from(round));
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RoundResponse> startFirstRound() {
        Round round = roundService.startFirstRound();
        return ApiResponse.success(RoundResponse.from(round));
    }

    @PutMapping("/duration/open")
    public ApiResponse<Void> updateOpenDuration(@RequestParam final int duration) {
        roundService.updateOpenDuration(duration);
        return ApiResponse.success(null);
    }

    @PutMapping("/duration/closed")
    public ApiResponse<Void> updateClosedDuration(@RequestParam final int duration) {
        roundService.updateClosedDuration(duration);
        return ApiResponse.success(null);
    }
}