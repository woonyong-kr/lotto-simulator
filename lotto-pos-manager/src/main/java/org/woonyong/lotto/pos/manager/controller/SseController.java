package org.woonyong.lotto.pos.manager.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.woonyong.lotto.pos.manager.config.PosManagerConfig;
import org.woonyong.lotto.pos.manager.dto.response.PosStatusResponse;
import org.woonyong.lotto.pos.manager.service.TerminalPoolManager;

@RestController
@RequestMapping("/api/stream")
public class SseController {
  private final TerminalPoolManager terminalPoolManager;
  private final PosManagerConfig config;

  public SseController(
      final TerminalPoolManager terminalPoolManager, final PosManagerConfig config) {
    this.terminalPoolManager = terminalPoolManager;
    this.config = config;
  }

  @GetMapping("/pos-status")
  public SseEmitter streamPosStatus() {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(
        () -> {
          try {
            PosStatusResponse status = collectPosStatus();
            emitter.send(SseEmitter.event().name("pos-status").data(status));
          } catch (Exception e) {
            emitter.completeWithError(e);
            executor.shutdown();
          }
        },
        0,
        config.getSseIntervalMs(),
        TimeUnit.MILLISECONDS);

    emitter.onCompletion(executor::shutdown);
    emitter.onTimeout(executor::shutdown);
    emitter.onError((throwable) -> executor.shutdown());

    return emitter;
  }

  private PosStatusResponse collectPosStatus() {
    int instancesPosCount = terminalPoolManager.getTotalInstanceCount();
    int totalCapacity = config.getMaxPosCapacity();
    int availablePosCount = terminalPoolManager.getAvailableCount();

    return new PosStatusResponse(instancesPosCount, totalCapacity, availablePosCount);
  }
}
