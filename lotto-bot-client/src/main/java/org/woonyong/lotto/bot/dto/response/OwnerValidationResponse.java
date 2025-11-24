package org.woonyong.lotto.bot.dto.response;

public class OwnerValidationResponse {
  private final boolean valid;
  private final String reason;

  private OwnerValidationResponse(final boolean valid, final String reason) {
    this.valid = valid;
    this.reason = reason;
  }

  public static OwnerValidationResponse valid() {
    return new OwnerValidationResponse(true, null);
  }

  public static OwnerValidationResponse invalid(final String reason) {
    return new OwnerValidationResponse(false, reason);
  }

  public boolean isValid() {
    return valid;
  }

  public String getReason() {
    return reason;
  }
}
