package org.woonyong.lotto.central.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
  private static final String ERROR_INVALID_TYPE = "유효하지 않은 티켓 타입입니다";
  private static final String ERROR_MANUAL_REQUIRES_NUMBERS = "수동구매는 6개의 번호가 필요합니다";
  private static final int REQUIRED_NUMBER_COUNT = 6;

  private final TicketService ticketService;

  public TicketController(final TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<TicketResponse>> purchaseTicket(
      @RequestBody final PurchaseTicketRequest request) {
    Ticket ticket = createTicketByType(request);
    TicketResponse response = TicketResponse.from(ticket);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
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
    throw new IllegalArgumentException(ERROR_INVALID_TYPE);
  }

  private Ticket purchaseManualTicket(final PurchaseTicketRequest request) {
    validateManualNumbers(request.getNumbers());
    LottoNumbers numbers = LottoNumbers.from(request.getNumbers());
    return ticketService.purchaseManualTicket(request.getRoundId(), numbers, request.getPosUid());
  }

  private Ticket purchaseAutoTicket(final PurchaseTicketRequest request) {
    return ticketService.purchaseAutoTicket(request.getRoundId(), request.getPosUid());
  }

  private void validateManualNumbers(final List<Integer> numbers) {
    if (numbers == null || numbers.size() != REQUIRED_NUMBER_COUNT) {
      throw new IllegalArgumentException(ERROR_MANUAL_REQUIRES_NUMBERS);
    }
  }
}
