package org.woonyong.lotto.pos.terminal.exception;

public class PosTerminalException extends RuntimeException {
    private final String errorCode;

    public PosTerminalException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PosTerminalException(final String errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static PosTerminalException of(final String errorCode, final String message) {
        return new PosTerminalException(errorCode, message);
    }

    public static PosTerminalException of(final String errorCode, final String message, final Throwable cause) {
        return new PosTerminalException(errorCode, message, cause);
    }
}