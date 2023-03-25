package io.howstheairtoday.appcommunityexternalapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ApiResponse<T> {

    private String msg;
    private Integer statusCode;
    private T data;

    public static <T> ApiResponse<T> res(final Integer statusCode, final String msg) {
        return ApiResponse.<T>builder()
            .msg(msg)
            .statusCode(statusCode)
            .build();
    }

    public static <T> ApiResponse<T> res(final Integer statusCode, final String msg, final T data) {
        return ApiResponse.<T>builder()
            .msg(msg)
            .statusCode(statusCode)
            .data(data)
            .build();
    }
}