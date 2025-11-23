package org.woonyong.lotto.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Dashboard is running";
    }
}