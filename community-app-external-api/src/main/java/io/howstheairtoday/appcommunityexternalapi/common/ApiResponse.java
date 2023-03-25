package io.howstheairtoday.appcommunityexternalapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ApiResponse<T> {

    private String msg;  // 에러 메시지 또는 성공 메시지
    private Integer statusCode;  // HTTP 상태 코드
    private T data;  // 데이터

    /**
     * HTTP 상태 코드와 에러 메시지만 있는 ApiResponse 객체를 반환합니다.
     * @param statusCode HTTP 상태 코드
     * @param msg 에러 메시지
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> res(final Integer statusCode, final String msg) {
        return ApiResponse.<T>builder()
            .msg(msg)
            .statusCode(statusCode)
            .build();
    }

    /**
     * HTTP 상태 코드와 에러 메시지, 데이터를 가진 ApiResponse 객체를 반환합니다.
     * @param statusCode HTTP 상태 코드
     * @param msg 에러 메시지
     * @param data 데이터
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> res(final Integer statusCode, final String msg, final T data) {
        return ApiResponse.<T>builder()
            .msg(msg)
            .statusCode(statusCode)
            .data(data)
            .build();
    }
}

