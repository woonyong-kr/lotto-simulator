package org.woonyong.lotto.pos.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.manager.domain.TerminalInfo;
import org.woonyong.lotto.pos.manager.dto.request.AllocateTerminalRequest;
import org.woonyong.lotto.pos.manager.dto.request.RegisterTerminalRequest;
import org.woonyong.lotto.pos.manager.dto.request.ReleaseTerminalRequest;
import org.woonyong.lotto.pos.manager.dto.response.AllocateTerminalResponse;
import org.woonyong.lotto.pos.manager.dto.response.RegisterTerminalResponse;
import org.woonyong.lotto.pos.manager.dto.response.ReleaseTerminalResponse;
import org.woonyong.lotto.pos.manager.service.PosTerminalClient;
import org.woonyong.lotto.pos.manager.service.TerminalPoolManager;

@RestController
@RequestMapping("/api/terminals")
public class TerminalController {
  private static final String ERROR_POOL_EMPTY = "POOL_EMPTY";
  private static final String ERROR_POOL_EMPTY_MESSAGE = "사용 가능한 터미널이 없습니다";
  private static final String ERROR_TERMINAL_NOT_FOUND = "TERMINAL_NOT_FOUND";
  private static final String ERROR_TERMINAL_NOT_FOUND_MESSAGE = "터미널을 찾을 수 없습니다";

  private final TerminalPoolManager terminalPoolManager;
  private final PosTerminalClient posTerminalClient;

  public TerminalController(
      final TerminalPoolManager terminalPoolManager, final PosTerminalClient posTerminalClient) {
    this.terminalPoolManager = terminalPoolManager;
    this.posTerminalClient = posTerminalClient;
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterTerminalResponse>> registerTerminal(
      @RequestBody final RegisterTerminalRequest request, final HttpServletRequest httpRequest) {

    String clientAddress = getClientAddress(httpRequest);
    String terminalAddress = "http://" + clientAddress + ":8400";

    terminalPoolManager.register(request.getTerminalId(), terminalAddress);

    RegisterTerminalResponse response =
        new RegisterTerminalResponse(true, terminalPoolManager.getAvailableCount());

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
  }

  @PostMapping("/allocate")
  public ResponseEntity<ApiResponse<AllocateTerminalResponse>> allocateTerminal(
      @RequestBody final AllocateTerminalRequest request) {

    TerminalInfo terminal = terminalPoolManager.allocate();

    if (terminal == null) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(ApiResponse.error(ERROR_POOL_EMPTY, ERROR_POOL_EMPTY_MESSAGE));
    }

    try {
      posTerminalClient.assignOwner(
          terminal.getAddress(),
          request.getBotUid(),
          request.getPosUid(),
          request.getBotClientUrl());

      AllocateTerminalResponse response =
          new AllocateTerminalResponse(terminal.getTerminalId(), terminal.getAddress());

      return ResponseEntity.ok(ApiResponse.success(response));
    } catch (Exception e) {
      terminalPoolManager.release(terminal.getTerminalId());
      throw e;
    }
  }

  @PostMapping("/release")
  public ResponseEntity<ApiResponse<ReleaseTerminalResponse>> releaseTerminal(
      @RequestBody final ReleaseTerminalRequest request) {

    TerminalInfo terminal = terminalPoolManager.findByTerminalId(request.getTerminalId());

    if (terminal == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(ERROR_TERMINAL_NOT_FOUND, ERROR_TERMINAL_NOT_FOUND_MESSAGE));
    }

    terminalPoolManager.release(request.getTerminalId());

    ReleaseTerminalResponse response =
        new ReleaseTerminalResponse(true, terminalPoolManager.getAvailableCount());

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  private String getClientAddress(final HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (xRealIp != null && !xRealIp.isEmpty()) {
      return xRealIp;
    }

    return request.getRemoteAddr();
  }
}
