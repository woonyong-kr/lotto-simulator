package org.woonyong.lotto.core.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> ApiResponse<T> success(final T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(final String code, final String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(ErrorResponse.of(code, message))
                .build();
    }
}