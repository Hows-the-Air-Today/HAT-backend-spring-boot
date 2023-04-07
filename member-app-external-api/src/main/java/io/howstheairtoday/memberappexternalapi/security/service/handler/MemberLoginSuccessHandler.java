package io.howstheairtoday.memberappexternalapi.security.service.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.google.gson.Gson;

import io.howstheairtoday.memberappexternalapi.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Spring Security - 로그인 인증 성공 작업 후처리
 */
@Log4j2
@RequiredArgsConstructor
public class MemberLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        log.info("🛠️ Login Success Handler -------------------- 🛠️");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("💡 authentication =====> " + authentication);
        log.info("💡 LOGINID =====> " + authentication.getName());

        Map<String, Object> claim = Map.of("loginId", authentication.getName());
        // AccessToken 유효기간 30분
        String accessToken = jwtUtil.generateToken(claim, 1/48);
        // RefreshToken 유효기간 7일
        String refreshToken = jwtUtil.generateToken(claim, 7);

        Gson gson = new Gson();

        Map<String, String> keyMap = Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}
