package org.woonyong.lotto.pos.manager.exception;

public class PosManagerException extends RuntimeException {
    private final String errorCode;

    public PosManagerException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PosManagerException(final String errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static PosManagerException of(final String errorCode, final String message) {
        return new PosManagerException(errorCode, message);
    }

    public static PosManagerException of(final String errorCode, final String message, final Throwable cause) {
        return new PosManagerException(errorCode, message, cause);
    }
}