package org.woonyong.lotto.core.domain;

public enum TicketType {
  MANUAL("수동"),
  AUTO("자동");

  private final String description;

  TicketType(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public boolean isManual() {
    return this == MANUAL;
  }

  public boolean isAuto() {
    return this == AUTO;
  }
}
