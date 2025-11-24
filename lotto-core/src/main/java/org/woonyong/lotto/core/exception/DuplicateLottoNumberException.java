package org.woonyong.lotto.core.exception;

public class DuplicateLottoNumberException extends LottoException {
  private static final String ERROR_CODE = "DUPLICATE_LOTTO_NUMBER";
  private static final String MESSAGE = "로또 번호는 중복될 수 없습니다.";

  public DuplicateLottoNumberException() {
    super(ERROR_CODE, MESSAGE);
  }
}
