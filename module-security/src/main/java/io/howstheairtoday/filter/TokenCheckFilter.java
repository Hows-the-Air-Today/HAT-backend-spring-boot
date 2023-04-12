package io.howstheairtoday.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;

import io.howstheairtoday.exception.AccessTokenException;
import io.howstheairtoday.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

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
         * [1] 요청 경로가 "/api/"로 시작하는 경우에만 필터링을 수행
         * 참고: path.startsWith("/api/v1/post/") = /api/v1/post/로 시작하는 URL 경로 접근 모두 허용
         * [2] Request Header에서 "Authorization" 헤더를 찾아 JWT 토큰을 추출
         * [3] 추출한 JWT 토큰의 유효성을 검사
         * [4] 토큰이 유효한 경우, 추출한 토큰에서 사용자 정보를 추출하여 SecurityContext에 저장
         * [5] 유효한 토큰이 아닌 경우, 401 Unauthorized 응답을 보냅니다.
         */
        if (path.startsWith("/api/v1/post/") || // TODO: Post와 Member 개발 완료 후 제거
            path.startsWith("/api/v1/airquality/") ||
            path.startsWith("/api/v1/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

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