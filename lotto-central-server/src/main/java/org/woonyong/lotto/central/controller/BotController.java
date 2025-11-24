package org.woonyong.lotto.central.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.central.dto.request.UpdateBotConfigRequest;
import org.woonyong.lotto.central.dto.response.BotResponse;
import org.woonyong.lotto.central.entity.Bot;
import org.woonyong.lotto.central.service.BotService;
import org.woonyong.lotto.core.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotService botService;

    public BotController(final BotService botService) {
        this.botService = botService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BotResponse>> createBot() {
        Bot bot = botService.createBot();
        BotResponse response = BotResponse.from(bot);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{botUid}")
    public ResponseEntity<ApiResponse<BotResponse>> getBot(@PathVariable final String botUid) {
        Bot bot = botService.getBot(botUid);
        BotResponse response = BotResponse.from(bot);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BotResponse>>> getBotList(
            @RequestParam(required = false) final Boolean active
    ) {
        List<Bot> bots = getFilteredBotList(active);
        List<BotResponse> responses = bots.stream()
                .map(BotResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PutMapping("/{botUid}/config")
    public ResponseEntity<ApiResponse<BotResponse>> updateConfig(
            @PathVariable final String botUid,
            @RequestBody final UpdateBotConfigRequest request
    ) {
        Bot bot = botService.updateConfig(
                botUid,
                request.getPurchaseIntervalMs(),
                request.getTicketsPerPurchase()
        );
        BotResponse response = BotResponse.from(bot);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{botUid}/deactivate")
    public ResponseEntity<ApiResponse<BotResponse>> deactivate(@PathVariable final String botUid) {
        Bot bot = botService.deactivate(botUid);
        BotResponse response = BotResponse.from(bot);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private List<Bot> getFilteredBotList(final Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            return botService.getActiveBotList();
        }
        return botService.getAllBotList();
    }
}
