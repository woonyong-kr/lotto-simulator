package org.woonyong.lotto.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.bot.dto.request.UpdateBotConfigRequest;
import org.woonyong.lotto.bot.dto.response.CreateBotResponse;
import org.woonyong.lotto.bot.dto.response.DeleteAllBotsResponse;
import org.woonyong.lotto.bot.dto.response.DeleteBotResponse;
import org.woonyong.lotto.bot.dto.response.UpdateBotConfigResponse;
import org.woonyong.lotto.bot.service.BotService;
import org.woonyong.lotto.core.dto.ApiResponse;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotService botService;

    public BotController(final BotService botService) {
        this.botService = botService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateBotResponse>> createBot() {
        String botUid = botService.createBot();
        if (botUid == null) {
            return ResponseEntity.ok(ApiResponse.error("BOT_CREATE_FAILED", "봇 생성 실패"));
        }
        CreateBotResponse response = new CreateBotResponse(botUid);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{botUid}/config")
    public ResponseEntity<ApiResponse<UpdateBotConfigResponse>> updateConfig(
            @PathVariable final String botUid,
            @RequestBody final UpdateBotConfigRequest request
    ) {
        boolean success = botService.updateConfig(
                botUid,
                request.getPurchaseIntervalMs(),
                request.getTicketsPerPurchase()
        );
        if (!success) {
            return ResponseEntity.ok(ApiResponse.error("BOT_CONFIG_UPDATE_FAILED", "봇 설정 변경 실패"));
        }
        UpdateBotConfigResponse response = new UpdateBotConfigResponse(
                botUid,
                request.getPurchaseIntervalMs(),
                request.getTicketsPerPurchase()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{botUid}")
    public ResponseEntity<ApiResponse<DeleteBotResponse>> deleteBot(
            @PathVariable final String botUid
    ) {
        boolean success = botService.deleteBot(botUid);
        if (!success) {
            return ResponseEntity.ok(ApiResponse.error("BOT_DELETE_FAILED", "봇 삭제 실패"));
        }
        DeleteBotResponse response = new DeleteBotResponse(botUid);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<DeleteAllBotsResponse>> deleteAllBots() {
        int deletedCount = botService.deleteAllBots();
        DeleteAllBotsResponse response = new DeleteAllBotsResponse(deletedCount);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
