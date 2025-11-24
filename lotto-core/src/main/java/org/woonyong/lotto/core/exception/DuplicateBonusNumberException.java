package org.woonyong.lotto.core.exception;

public class DuplicateBonusNumberException extends LottoException {
  private static final String ERROR_CODE = "DUPLICATE_BONUS_NUMBER";
  private static final String MESSAGE = "보너스 번호는 당첨 번호와 중복될 수 없습니다.";

  public DuplicateBonusNumberException() {
    super(ERROR_CODE, MESSAGE);
  }
}
