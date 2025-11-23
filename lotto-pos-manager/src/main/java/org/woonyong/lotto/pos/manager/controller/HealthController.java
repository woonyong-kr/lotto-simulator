package org.woonyong.lotto.pos.manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pos-manager")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "POS Manager is running";
    }
}