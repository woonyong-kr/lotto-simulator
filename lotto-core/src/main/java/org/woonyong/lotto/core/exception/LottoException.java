package org.woonyong.lotto.core.exception;

import lombok.Getter;

@Getter
public class LottoException extends RuntimeException {
    private final String errorCode;

    public LottoException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static LottoException of(final String errorCode, final String message) {
        return new LottoException(errorCode, message);
    }
}
