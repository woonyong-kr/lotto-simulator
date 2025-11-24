package org.woonyong.lotto.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.bot.dto.response.OwnerValidationResponse;
import org.woonyong.lotto.bot.manager.BotInstanceManager;

@RestController
@RequestMapping("/api/health")
public class OwnerHealthController {
    private static final String ERROR_BOT_NOT_FOUND = "봇이 인스턴스되어 있지 않습니다";

    private final BotInstanceManager botInstanceManager;

    public OwnerHealthController(final BotInstanceManager botInstanceManager) {
        this.botInstanceManager = botInstanceManager;
    }

    @GetMapping("/owner")
    public ResponseEntity<OwnerValidationResponse> validateOwner(
            @RequestParam final String botUid
    ) {
        boolean isRunning = botInstanceManager.isInstanceRunning(botUid);
        if (!isRunning) {
            return ResponseEntity.ok(OwnerValidationResponse.invalid(ERROR_BOT_NOT_FOUND));
        }
        return ResponseEntity.ok(OwnerValidationResponse.valid());
    }
}
