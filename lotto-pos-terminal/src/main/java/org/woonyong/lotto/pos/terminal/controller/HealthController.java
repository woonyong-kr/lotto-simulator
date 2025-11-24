package org.woonyong.lotto.pos.terminal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pos-terminal")
public class HealthController {

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("POS Terminal is running");
  }
}
