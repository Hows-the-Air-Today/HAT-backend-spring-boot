package io.howstheairtoday.filter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MemberLoginFilter extends AbstractAuthenticationProcessingFilter {

    public MemberLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request) {

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
    public Authentication attemptAuthentication (HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException {

        log.info("🛠️ MemberLoginFilter -------------------- 🛠️");

        if (request.getMethod().equalsIgnoreCase("GET")) {
            log.info("============ GET METHOD NOT SUPPORT ============");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        log.info("💡 jsonData =====> " + jsonData);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            jsonData.get("loginId"), jsonData.get("loginPassword"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
