package org.woonyong.lotto.central.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.woonyong.lotto.central.dto.request.UpdatePosStatusRequest;
import org.woonyong.lotto.central.dto.response.PosResponse;
import org.woonyong.lotto.central.entity.Pos;
import org.woonyong.lotto.central.service.PosService;
import org.woonyong.lotto.core.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/pos")
public class PosController {

    private final PosService posService;

    public PosController(final PosService posService) {
        this.posService = posService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PosResponse>> createPos() {
        Pos pos = posService.createPos();
        PosResponse response = PosResponse.from(pos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/{posUid}/status")
    public ResponseEntity<ApiResponse<PosResponse>> updateStatus(
            @PathVariable final String posUid,
            @RequestBody final UpdatePosStatusRequest request
    ) {
        Pos pos = posService.updateStatus(posUid, request.isActive());
        PosResponse response = PosResponse.from(pos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{posUid}")
    public ResponseEntity<ApiResponse<PosResponse>> getPos(
            @PathVariable final String posUid
    ) {
        Pos pos = posService.getPos(posUid);
        PosResponse response = PosResponse.from(pos);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PosResponse>>> getPosList(
            @RequestParam(required = false) final Boolean active
    ) {
        List<Pos> posList = getFilteredPosList(active);
        List<PosResponse> responses = posList.stream()
                .map(PosResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    private List<Pos> getFilteredPosList(final Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            return posService.getActivePosList();
        }
        return posService.getAllPosList();
    }
}