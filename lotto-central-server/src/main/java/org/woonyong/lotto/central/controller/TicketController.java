package org.woonyong.lotto.central.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.central.dto.request.PurchaseTicketRequest;
import org.woonyong.lotto.central.dto.response.TicketResponse;
import org.woonyong.lotto.central.entity.Ticket;
import org.woonyong.lotto.central.service.TicketService;
import org.woonyong.lotto.core.domain.LottoNumbers;
import org.woonyong.lotto.core.dto.ApiResponse;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private static final String TYPE_MANUAL = "MANUAL";
    private static final String TYPE_AUTO = "AUTO";

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> purchaseTicket(
            @RequestBody final PurchaseTicketRequest request) {
        Ticket ticket = createTicketByType(request);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping("/{ticketNumber}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicket(
            @PathVariable final String ticketNumber) {
        Ticket ticket = ticketService.findByTicketNumber(ticketNumber);
        TicketResponse response = TicketResponse.from(ticket);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Ticket createTicketByType(final PurchaseTicketRequest request) {
        if (TYPE_MANUAL.equals(request.getType())) {
            return purchaseManualTicket(request);
        }
        if (TYPE_AUTO.equals(request.getType())) {
            return purchaseAutoTicket(request);
        }
        throw new IllegalArgumentException("유효하지 않은 티켓 타입입니다");
    }

    private Ticket purchaseManualTicket(final PurchaseTicketRequest request) {
        if (request.getNumbers() == null || request.getNumbers().size() != 6) {
            throw new IllegalArgumentException("수동구매는 6개의 번호가 필요합니다");
        }
        LottoNumbers numbers = LottoNumbers.from(request.getNumbers());
        return ticketService.purchaseManualTicket(request.getRoundId(), numbers);
    }

    private Ticket purchaseAutoTicket(final PurchaseTicketRequest request) {
        return ticketService.purchaseAutoTicket(request.getRoundId());
    }
}