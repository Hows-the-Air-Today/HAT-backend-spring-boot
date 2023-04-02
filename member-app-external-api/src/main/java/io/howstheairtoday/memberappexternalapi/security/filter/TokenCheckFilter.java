package io.howstheairtoday.memberappexternalapi.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;

import io.howstheairtoday.memberappexternalapi.exception.AccessTokenException;
import io.howstheairtoday.memberappexternalapi.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * JWT 인증 필터
 */
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // Access Token을 검증하는 메서드
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        String headerStr = request.getHeader("Authorization");
        if (headerStr == null || headerStr.length() < 8) {
            // Authorization 헤더의 값이 null 이거나 길이가 8 미만인 경우,
            // 유효한 JWT 토큰이 아니므로 AccessTokenException 예외를 발생
            // - Authorization 헤더의 값은 보통 "Bearer [JWT Token]" 형식으로 구성
            // - "Bearer " (Bearer와 공백)를 포함한 최소 8자리의 문자열이 필요
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if (tokenType.equalsIgnoreCase("Bearer") == false) {
            // Bearer 토큰 유형이 아닌 경우 AccessTokenException 예외가 발생
            // - 헤더 값에서 Bearer가 없거나 대소문자가 일치하지 않는 경우
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("🚨 MalformedJwtException -------------------- 🚨");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("🚨 SignatureException -------------------- 🚨");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("🚨 ExpiredJwtException -------------------- 🚨");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        String path = request.getRequestURI();
        /**
         * /api/로 시작하는 모든 경로의 호출에 사용되고 사용자는 해당 경로에 다음과 같은 상황으로 접근
         * [1] Access Token이 없는 경우 - 토큰이 없다는 메시지 전달
         * [2] Access Token이 잘못된 경우(서명 혹은 구성, 기타 에러) - 잘못된 토큰이라는 메시지 전달
         * [3] Access Token이 존재하지만 오래된(expired) 값인 경우 - 토큰을 갱신하라는 메시지 전달
         */
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("🛠️ Token Check Filter -------------------- 🛠️");
        log.info("💡 JWTUtil =====> " + jwtUtil);

        try {
            validateAccessToken(request);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException accessTokenException) {
            accessTokenException.sendResponseError(response);
        }
    }
}
