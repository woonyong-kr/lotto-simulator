package org.woonyong.lotto.core.constant;

import java.time.Duration;

public final class WebClientConstants {
  public static final int MAX_CONNECTIONS = 10;
  public static final Duration MAX_IDLE_TIME = Duration.ofSeconds(20);
  public static final String DASHBOARD_REPORT_FAILURE_MESSAGE = "대시보드 리포트 실패: ";
  public static final int MAX_IN_MEMORY_SIZE = 1024 * 1024;

  private WebClientConstants() {}
}
