package org.woonyong.lotto.core.exception;

import org.woonyong.lotto.core.constant.LottoConstants;

public class InvalidLottoNumberException extends LottoException {
    private static final String ERROR_CODE = "INVALID_LOTTO_NUMBER";
    private static final String MESSAGE_FORMAT = "로또 번호는 %d와 %d 사이여야 합니다. 입력값: %d";

    public InvalidLottoNumberException(final int value) {
        super(ERROR_CODE, String.format(MESSAGE_FORMAT,
                LottoConstants.MIN_NUMBER,
                LottoConstants.MAX_NUMBER,
                value));
    }
}
