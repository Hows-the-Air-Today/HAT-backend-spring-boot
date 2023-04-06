package io.howstheairtoday.exception;

import java.util.Date;
import java.util.Map;

import org.springframework.http.MediaType;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Access Token 검증 - 예외 처리
 * AccessToken에 문제가 있는 경우 발생할 사용자 정의 예외 클래스
 */
public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR {

        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401, "TOken type Bearer"),
        MALFORM(403, "Malformed Token"),
        BADSIGN(403, "BadSignatured Token"),
        EXPIRED(403, "Expired Token");

        private int stauts;
        private String msg;

        TOKEN_ERROR(int stauts, String msg) {
            this.stauts = stauts;
            this.msg = msg;
        }

        public int getStatus() {
            return this.stauts;
        }

        public Object getMsg() {
            return this.msg;
        }
    }

    public AccessTokenException(TOKEN_ERROR error) {
        super(error.name());
        this.token_error = error;
    }

    // 에러 전송 메서드
    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String responseStr = gson.toJson(Map.of(
            "msg", token_error.getMsg(),
            "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
