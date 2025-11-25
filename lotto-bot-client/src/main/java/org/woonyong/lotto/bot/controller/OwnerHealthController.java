package org.woonyong.lotto.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.bot.dto.request.OwnerValidationRequest;
import org.woonyong.lotto.bot.dto.response.OwnerValidationResponse;
import org.woonyong.lotto.bot.manager.BotInstanceManager;
import org.woonyong.lotto.core.dto.ApiResponse;

@RestController
@RequestMapping("/api/health")
public class OwnerHealthController {
  private static final String ERROR_BOT_NOT_FOUND = "봇이 인스턴스되어 있지 않습니다";

  private final BotInstanceManager botInstanceManager;

  public OwnerHealthController(final BotInstanceManager botInstanceManager) {
    this.botInstanceManager = botInstanceManager;
  }

  @PostMapping("/owner")
  public ResponseEntity<ApiResponse<OwnerValidationResponse>> validateOwner(
      @RequestBody final OwnerValidationRequest request) {
    boolean isRunning = botInstanceManager.isInstanceRunning(request.getOwnerBotUid());
    if (!isRunning) {
      return ResponseEntity.ok(ApiResponse.success(OwnerValidationResponse.invalid(ERROR_BOT_NOT_FOUND)));
    }
    return ResponseEntity.ok(ApiResponse.success(OwnerValidationResponse.valid()));
  }
}
