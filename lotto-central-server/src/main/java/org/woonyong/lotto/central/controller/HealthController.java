package org.woonyong.lotto.central.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/central")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Central Server is running";
    }
}