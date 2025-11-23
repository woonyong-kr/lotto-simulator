package org.woonyong.lotto.core.exception;

import lombok.Getter;

@Getter
public class LottoException extends RuntimeException {
    private final String errorCode;

    public LottoException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static LottoException of(String errorCode, String message) {
        return new LottoException(errorCode, message);
    }
}
