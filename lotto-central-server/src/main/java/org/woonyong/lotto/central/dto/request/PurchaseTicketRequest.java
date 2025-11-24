package org.woonyong.lotto.central.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class PurchaseTicketRequest {
  @NotNull(message = "회차 ID는 필수입니다")
  @Positive(message = "회차 ID는 양수여야 합니다")
  private Long roundId;

  @NotNull(message = "POS UID는 필수입니다")
  private String posUid;

  @NotNull(message = "티켓 타입은 필수입니다")
  private String type;

  private List<Integer> numbers;

  public PurchaseTicketRequest() {}

  public PurchaseTicketRequest(
      final Long roundId, final String posUid, final String type, final List<Integer> numbers) {
    this.roundId = roundId;
    this.posUid = posUid;
    this.type = type;
    this.numbers = numbers;
  }

  public Long getRoundId() {
    return roundId;
  }

  public void setRoundId(final Long roundId) {
    this.roundId = roundId;
  }

  public String getPosUid() {
    return posUid;
  }

  public void setPosUid(final String posUid) {
    this.posUid = posUid;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public List<Integer> getNumbers() {
    return numbers;
  }

  public void setNumbers(final List<Integer> numbers) {
    this.numbers = numbers;
  }
}
