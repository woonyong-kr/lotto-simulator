package org.woonyong.lotto.pos.terminal.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequest {
  @NotNull(message = "구매 개수는 필수입니다")
  @Min(value = 1, message = "구매 개수는 1개 이상이어야 합니다")
  private Integer count;

  public PurchaseRequest() {}

  public Integer getCount() {
    return count;
  }

  public void setCount(final Integer count) {
    this.count = count;
  }
}
