package org.woonyong.lotto.pos.manager.dto.response;

public class PosStatusResponse {
    private final int instancesPosCount;
    private final int totalCapacity;
    private final int availablePosCount;

    public PosStatusResponse(final int instancesPosCount, final int totalCapacity, final int availablePosCount) {
        this.instancesPosCount = instancesPosCount;
        this.totalCapacity = totalCapacity;
        this.availablePosCount = availablePosCount;
    }

    public int getInstancesPosCount() {
        return instancesPosCount;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getAvailablePosCount() {
        return availablePosCount;
    }
}