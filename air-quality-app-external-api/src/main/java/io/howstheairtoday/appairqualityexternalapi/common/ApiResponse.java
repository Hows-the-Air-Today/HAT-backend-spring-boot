package io.howstheairtoday.appairqualityexternalapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    public static final String SUCCESS_STATUS = "success";
    public static final String FAIL_STATUS = "fail";
    public static final String ERROR_STATUS = "error";

    private final int statusCode;
    private final String status;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(String message, T data) {

        return ApiResponse.<T>builder()
                .statusCode(200)
                .status(SUCCESS_STATUS)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> fail(int statusCode, String message) {

        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status(FAIL_STATUS)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {

        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status(ERROR_STATUS)
                .message(message)
                .build();
    }
}
