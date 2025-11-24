package org.woonyong.lotto.central.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.central.dto.response.RoundDetailResponse;
import org.woonyong.lotto.central.dto.response.RoundResponse;
import org.woonyong.lotto.central.entity.Round;
import org.woonyong.lotto.central.service.RoundService;
import org.woonyong.lotto.central.service.RoundStatisticsService;
import org.woonyong.lotto.core.dto.ApiResponse;

@RestController
@RequestMapping("/api/rounds")
public class RoundController {
  private static final String ERROR_NO_CURRENT_ROUND = "진행 중인 회차가 없습니다";

  private final RoundService roundService;
  private final RoundStatisticsService roundStatisticsService;

  public RoundController(
      final RoundService roundService, final RoundStatisticsService roundStatisticsService) {
    this.roundService = roundService;
    this.roundStatisticsService = roundStatisticsService;
  }

  @GetMapping("/current")
  public ResponseEntity<ApiResponse<RoundResponse>> getCurrentRound() {
    Round round =
        roundService
            .getCurrentRound()
            .orElseThrow(() -> new IllegalStateException(ERROR_NO_CURRENT_ROUND));
    return ResponseEntity.ok(ApiResponse.success(RoundResponse.from(round)));
  }

  @GetMapping("/recent")
  public ResponseEntity<List<RoundDetailResponse>> getRecentRounds(
      @RequestParam(defaultValue = "5") final int count) {
    List<RoundDetailResponse> rounds = roundStatisticsService.getRecentRounds();
    return ResponseEntity.ok(rounds);
  }

  @PutMapping("/duration/open")
  public ResponseEntity<ApiResponse<Void>> updateOpenDuration(@RequestParam final int duration) {
    roundService.updateOpenDuration(duration);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PutMapping("/duration/closed")
  public ResponseEntity<ApiResponse<Void>> updateClosedDuration(@RequestParam final int duration) {
    roundService.updateClosedDuration(duration);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
