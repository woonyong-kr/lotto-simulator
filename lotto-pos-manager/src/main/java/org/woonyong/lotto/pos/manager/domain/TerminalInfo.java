package org.woonyong.lotto.pos.manager.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class TerminalInfo {
    private final String terminalId;
    private final String address;
    private String containerId;
    private LocalDateTime lastHealthCheck;
    private boolean healthy;
    private LocalDateTime createdAt;

    public TerminalInfo(final String terminalId, final String address) {
        this.terminalId = terminalId;
        this.address = address;
        this.healthy = true;
        this.createdAt = LocalDateTime.now();
    }

    public TerminalInfo(final String terminalId, final String address, final String containerId) {
        this.terminalId = terminalId;
        this.address = address;
        this.containerId = containerId;
        this.healthy = true;
        this.createdAt = LocalDateTime.now();
    }

    public void markHealthy() {
        this.healthy = true;
        this.lastHealthCheck = LocalDateTime.now();
    }

    public void markUnhealthy() {
        this.healthy = false;
        this.lastHealthCheck = LocalDateTime.now();
    }

    public void updateContainerId(final String containerId) {
        this.containerId = containerId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public String getAddress() {
        return address;
    }

    public String getContainerId() {
        return containerId;
    }

    public LocalDateTime getLastHealthCheck() {
        return lastHealthCheck;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalInfo that)) {
            return false;
        }
        return Objects.equals(terminalId, that.terminalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminalId);
    }

    @Override
    public String toString() {
        return "TerminalInfo{" +
                "terminalId='" + terminalId + '\'' +
                ", address='" + address + '\'' +
                ", healthy=" + healthy +
                '}';
    }
}