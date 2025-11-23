package org.woonyong.lotto.pos.terminal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pos-terminal")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "POS Terminal is running";
    }
}