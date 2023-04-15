package io.howstheairtoday.filter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;

import io.howstheairtoday.exception.RefreshTokenException;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import io.howstheairtoday.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JwtUtil jwtUtil;
    private Member member;
    private MemberRepository memberRepository;

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        // JSON 데이터에서 loginId, loginPassword 값을 Map으로 처리
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    //AccessToken 검증
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("🛠️ Access Token has expired -------------------- ❌");
        } catch (Exception exception) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    //RefreshToken 검증
    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException malformedJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        } catch (Exception exception) {
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    //Token을 전송하는 메서드
    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue, "refreshToken", refreshTokenValue));
        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        /**
         * [1] 필터는 요청이 특정 경로(refreshPath)와 일치하는지 확인
         * [2] 일치하는 경우 JwtUtil을 사용하여 토큰을 재발급
         * [3] 그렇지 않은 경우, "SKIP: Refresh Token Filter" 로그 메시지를 출력하고,
         * [4] 다음 필터 체인으로 요청을 전달합니다.
         */
        String path = request.getRequestURI();
        if (!path.equals(refreshPath)) {
            log.info("🛠️ SKIP: Refresh Token Filter -------------------- 🛠️");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("🛠️ RUN: Refresh Token Filter -------------------- 🛠️");

        // 전송된 JSON에서 AccessToken과 RefreshToken을 받아옴
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("💡 AccessToken =====> " + accessToken);
        log.info("💡 RefreshToken =====> " + refreshToken);

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null;

        try {
            refreshClaims = checkRefreshToken(refreshToken);
            log.info("💡 RefreshClaims =====> " + refreshClaims);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return;
        }

        /**
         * 만료 시간과 현재 시간의 간격 계산
         * 만일 3일 미만인 경우, Refresh Token 재발급
         */
        Integer exp = (Integer)refreshClaims.get("exp");

        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());

        long gapTime = (expTime.getTime() - current.getTime());

        log.info("🕑 Current Time =====> " + current);
        log.info("💣 EXP Time =====> " + expTime);
        log.info("💡 GAP Time =====> " + gapTime);

        String loginId = (String)refreshClaims.get("loginId");
        String memberId = (String)refreshClaims.get("memberId");
        String accessTokenValue = jwtUtil.generateToken(Map.of("loginId", loginId), 30, memberId);
        String refreshTokenValue = tokens.get("refreshToken");
        if (gapTime < (1000 * 60 * 60 * 24 * 3)) {
            log.info("🛠️ Refresh Token Required -------------------- 🛠️");
            refreshTokenValue = jwtUtil.generateToken(Map.of("loginId", loginId), 60 * 24 * 3, memberId);
        }

        log.info("🛠️ Refresh Token Result -------------------- 🛠️");
        log.info("💡 Access Token =====> " + accessTokenValue);
        log.info("💡 Refresh Token =====> " + refreshTokenValue);

        sendTokens(accessTokenValue, refreshTokenValue, response);
    }
}