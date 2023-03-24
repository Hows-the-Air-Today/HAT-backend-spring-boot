package io.howstheairtoday.modulecore.security.filter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.google.gson.Gson;

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

    // 파라미터를 읽어서 Map으로 만들어주는 메서드
    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        // JSON 데이터를 분석해 loginId, loginPassword 전달 값을 Map으로 처리
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {

        log.info("🛠️ MemberLoginFilter -------------------- 🛠️");

        if (request.getMethod().equalsIgnoreCase("GET")) {
            log.info("============ GET METHOD NOT SUPPORT ============");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);
        log.info("💡 jsonData =====> " + jsonData);
        // 회원 가입 Service 로직 단계에서 확인 예정
        /*
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            jsonData.get("loginId"), jsonData.get("loginPassword"));

        return getAuthenticationManager().authenticate(authenticationToken);
         */
        return null;
    }
}
