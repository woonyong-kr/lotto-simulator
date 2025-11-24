package org.woonyong.lotto.bot.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.woonyong.lotto.bot.domain.BotInstance;
import org.woonyong.lotto.bot.domain.HealthStatus;
import org.woonyong.lotto.bot.domain.PosTerminalConnection;

public class BotStatusResponse {
  private final LocalDateTime timestamp;
  private final Summary summary;
  private final List<BotInfo> bots;

  public BotStatusResponse(
      final int maxBotCapacity, final int activeBotCount, final List<BotInfo> bots) {
    this.timestamp = LocalDateTime.now();
    this.summary = new Summary(activeBotCount, maxBotCapacity);
    this.bots = bots;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Summary getSummary() {
    return summary;
  }

  public List<BotInfo> getBots() {
    return bots;
  }

  public static class Summary {
    private final int activeBotCount;
    private final int maxBotCapacity;

    public Summary(final int activeBotCount, final int maxBotCapacity) {
      this.activeBotCount = activeBotCount;
      this.maxBotCapacity = maxBotCapacity;
    }

    public int getActiveBotCount() {
      return activeBotCount;
    }

    public int getMaxBotCapacity() {
      return maxBotCapacity;
    }
  }

  public static class BotInfo {
    private final String botUid;
    private final boolean isRunning;
    private final Long avgResponseTime;
    private final List<PosInfo> posTerminals;

    public BotInfo(
        final String botUid,
        final boolean isRunning,
        final Long avgResponseTime,
        final List<PosInfo> posTerminals) {
      this.botUid = botUid;
      this.isRunning = isRunning;
      this.avgResponseTime = avgResponseTime;
      this.posTerminals = posTerminals;
    }

    public static BotInfo from(final BotInstance instance, final int failureThreshold) {
      List<PosInfo> posInfos =
          instance.getAllPosConnections().stream()
              .map(conn -> PosInfo.from(conn, failureThreshold))
              .toList();
      return new BotInfo(
          instance.getBotUid(),
          instance.isRunning(),
          instance.calculateAverageResponseTime(),
          posInfos);
    }

    public String getBotUid() {
      return botUid;
    }

    public boolean isRunning() {
      return isRunning;
    }

    public Long getAvgResponseTime() {
      return avgResponseTime;
    }

    public List<PosInfo> getPosTerminals() {
      return posTerminals;
    }
  }

  public static class PosInfo {
    private final String posUid;
    private final HealthStatus healthStatus;
    private final boolean isRunning;

    public PosInfo(final String posUid, final HealthStatus healthStatus, final boolean isRunning) {
      this.posUid = posUid;
      this.healthStatus = healthStatus;
      this.isRunning = isRunning;
    }

    public static PosInfo from(final PosTerminalConnection connection, final int failureThreshold) {
      return new PosInfo(
          connection.getPosUid(),
          connection.getHealthStatus(failureThreshold),
          connection.isRunning());
    }

    public String getPosUid() {
      return posUid;
    }

    public HealthStatus getHealthStatus() {
      return healthStatus;
    }

    public boolean isRunning() {
      return isRunning;
    }
  }
}
