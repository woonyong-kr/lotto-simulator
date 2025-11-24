package org.woonyong.lotto.central.dto.request;

import static org.woonyong.lotto.core.constant.LottoConstants.*;

import jakarta.validation.constraints.Size;
import java.util.List;

public class DrawRoundRequest {
  @Size(min = LOTTO_NUMBERS_COUNT, max = LOTTO_NUMBERS_COUNT, message = "당첨 번호는 6개여야 합니다")
  private List<Integer> winningNumbers;

  private Integer bonusNumber;

  protected DrawRoundRequest() {}

  public DrawRoundRequest(final List<Integer> winningNumbers, final Integer bonusNumber) {
    this.winningNumbers = winningNumbers;
    this.bonusNumber = bonusNumber;
  }

  public List<Integer> getWinningNumbers() {
    return winningNumbers;
  }

  public Integer getBonusNumber() {
    return bonusNumber;
  }
}
