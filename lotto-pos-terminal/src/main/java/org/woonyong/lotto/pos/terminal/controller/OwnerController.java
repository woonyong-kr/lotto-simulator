package org.woonyong.lotto.pos.terminal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.woonyong.lotto.core.dto.ApiResponse;
import org.woonyong.lotto.pos.terminal.dto.request.AssignOwnerRequest;
import org.woonyong.lotto.pos.terminal.dto.response.OwnerResponse;
import org.woonyong.lotto.pos.terminal.service.OwnerManager;
import org.woonyong.lotto.pos.terminal.service.TerminalInitializer;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    private static final String ERROR_ALREADY_ASSIGNED = "ALREADY_ASSIGNED";
    private static final String ERROR_ALREADY_ASSIGNED_MESSAGE = "소유자가 이미 존재합니다";

    private final OwnerManager ownerManager;
    private final TerminalInitializer terminalInitializer;

    public OwnerController(final OwnerManager ownerManager,
                          final TerminalInitializer terminalInitializer) {
        this.ownerManager = ownerManager;
        this.terminalInitializer = terminalInitializer;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OwnerResponse>> assignOwner(
            @RequestBody final AssignOwnerRequest request) {

        if (ownerManager.hasOwner()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(ERROR_ALREADY_ASSIGNED, ERROR_ALREADY_ASSIGNED_MESSAGE));
        }

        ownerManager.assignOwner(request.getBotUid(), request.getPosUid(), request.getBotClientUrl());

        OwnerResponse response = new OwnerResponse(
                terminalInitializer.getTerminalId(),
                ownerManager.getAssignedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

}