package org.woonyong.lotto.pos.terminal.domain;

import static org.woonyong.lotto.core.constant.FormatConstants.*;

import java.util.Objects;
import java.util.UUID;

public final class TerminalId {

  private final String value;

  public static TerminalId generate() {
    String uuid = UUID.randomUUID().toString().substring(0, UUID_LENGTH);
    return new TerminalId(TERMINAL_PREFIX + uuid);
  }

  public static TerminalId of(final String value) {
    return new TerminalId(value);
  }

  private TerminalId(final String value) {
    validateNotBlank(value);
    this.value = value;
  }

  private void validateNotBlank(final String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("터미널 ID는 빈 값일 수 없습니다");
    }
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TerminalId that)) {
      return false;
    }
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
