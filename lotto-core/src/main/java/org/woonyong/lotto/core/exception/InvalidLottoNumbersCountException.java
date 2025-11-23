package org.woonyong.lotto.core.exception;

import org.woonyong.lotto.core.constant.LottoConstants;

public class InvalidLottoNumbersCountException extends LottoException {
    private static final String ERROR_CODE = "INVALID_LOTTO_NUMBERS_COUNT";
    private static final String MESSAGE_FORMAT = "로또 번호는 정확히 %d개여야 합니다. 입력 개수: %d";

    public InvalidLottoNumbersCountException(final int actualCount) {
        super(ERROR_CODE, String.format(MESSAGE_FORMAT,
                LottoConstants.NUMBERS_COUNT,
                actualCount));
    }
}
