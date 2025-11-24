package org.woonyong.lotto.pos.terminal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.terminal.dto.request.PurchaseRequest;
import org.woonyong.lotto.pos.terminal.dto.response.PurchaseResponse;
import org.woonyong.lotto.pos.terminal.service.CentralServerClient;
import org.woonyong.lotto.pos.terminal.service.OwnerManager;
import org.woonyong.lotto.pos.terminal.service.ResponseTimeCollector;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private static final String ERROR_NO_OWNER = "NO_OWNER";
    private static final String ERROR_NO_OWNER_MESSAGE = "할당된 소유자가 없습니다";

    private final OwnerManager ownerManager;
    private final ResponseTimeCollector responseTimeCollector;
    private final CentralServerClient centralServerClient;

    public TicketController(final OwnerManager ownerManager,
                           final ResponseTimeCollector responseTimeCollector,
                           final CentralServerClient centralServerClient) {
        this.ownerManager = ownerManager;
        this.responseTimeCollector = responseTimeCollector;
        this.centralServerClient = centralServerClient;
    }

    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<PurchaseResponse>> purchaseTickets(
            @RequestBody final PurchaseRequest request) {

        if (!ownerManager.hasOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(ERROR_NO_OWNER, ERROR_NO_OWNER_MESSAGE));
        }

        long startTime = System.currentTimeMillis();

        try {
            String ticketNumber = centralServerClient.purchaseAutoTicket(request.getCount(), ownerManager.getPosUid());

            long responseTime = System.currentTimeMillis() - startTime;
            responseTimeCollector.record(responseTime);

            PurchaseResponse response = new PurchaseResponse(ticketNumber, responseTime);
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            responseTimeCollector.record(responseTime);
            throw e;
        }
    }
}