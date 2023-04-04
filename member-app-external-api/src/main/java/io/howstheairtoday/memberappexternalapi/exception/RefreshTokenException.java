package io.howstheairtoday.memberappexternalapi.exception;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Refresh Token 검증 - 예외 처리
 * RefreshToken에 문제가 있는 경우 발생할 사용자 정의 예외 클래스
 */
public class RefreshTokenException extends RuntimeException {

    private ErrorCase errorCase;

    public enum ErrorCase {
        NO_ACCESS, NO_REFRESH, OLD_REFRESH
    }

    public RefreshTokenException(ErrorCase errorCase) {
        super(errorCase.name());
        this.errorCase = errorCase;
    }

    public void sendResponseError(HttpServletResponse response) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", errorCase.name(), "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
