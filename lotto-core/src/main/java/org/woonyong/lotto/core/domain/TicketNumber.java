package org.woonyong.lotto.core.domain;

import static org.woonyong.lotto.core.constant.FormatConstants.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public final class TicketNumber {
  private static final String ERROR_MESSAGE = "티켓 번호는 비어있을 수 없습니다.";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

  private final String value;

  public static TicketNumber generate() {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    String uuid = UUID.randomUUID().toString().substring(0, UUID_LENGTH);
    return new TicketNumber(timestamp + DELIMITER + uuid);
  }

  public static TicketNumber of(final String value) {
    return new TicketNumber(value);
  }

  private TicketNumber(final String value) {
    validateNotEmpty(value);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  private void validateNotEmpty(final String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(ERROR_MESSAGE);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TicketNumber that)) {
      return false;
    }
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
