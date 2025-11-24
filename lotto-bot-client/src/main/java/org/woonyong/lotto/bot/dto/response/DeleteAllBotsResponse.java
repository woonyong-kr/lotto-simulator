package org.woonyong.lotto.bot.dto.response;

public class DeleteAllBotsResponse {
    private final int deletedCount;

    public DeleteAllBotsResponse(final int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }
}
