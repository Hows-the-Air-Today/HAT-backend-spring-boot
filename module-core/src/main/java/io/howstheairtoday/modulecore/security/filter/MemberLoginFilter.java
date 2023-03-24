package io.howstheairtoday.modulecore.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;


/**
 * 토큰 인증을 위한 Security 필터 설정
 * JWT의 Access Token과 Refresh Token을 얻으려는 단계를 구현하기 위한 설정
 */
@Log4j2
public class MemberLoginFilter extends AbstractAuthenticationProcessingFilter {

    public MemberLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {

        log.info("🛠️ MemberLoginFilter -------------------- 🛠️");

        return null;
    }
}
