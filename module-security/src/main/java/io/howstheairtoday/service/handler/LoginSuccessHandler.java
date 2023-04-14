package io.howstheairtoday.service.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.google.gson.Gson;

import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import io.howstheairtoday.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Spring Security에서 로그인이 성공했을 때 처리할 핸들러 클래스
 */
@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * HttpServletRequest 객체 - 로그인 요청 정보를 담고 있음
     * HttpServletResponse 객체 - 로그인 응답 정보를 담고 있음
     * Authentication 객체 - 인증에 성공한 사용자 정보를 담고 있음.
     */
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException {

        log.info("🛠️ Login Success Handler -------------------- 🛠️");

        /**
         * onAuthenticationSuccess() 메소드에서 response.setContentType()을 통해
         * 로그인 성공 시 전송되는 응답 정보의 ContentType을 JSON으로 설정
         */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("💡 authentication =====> " + authentication);
        log.info("💡 LOGINID =====> " + authentication.getName());

        // Member 엔티티를 조회합니다. 로그인한 사용자의 ID를 기준으로 조회합니다.
        String loginId = authentication.getName();
        Member member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new RuntimeException("등록된 아이디가 없습니다."));

        Map<String, Object> claim = Map.of("loginId", authentication.getName());
        // AccessToken 유효기간 30분
        String accessToken = jwtUtil.generateToken(claim, 30, String.valueOf(member.getMemberId()));
        // RefreshToken 유효기간 7일
        String refreshToken = jwtUtil.generateToken(claim, 7 * 24 * 60, String.valueOf(member.getMemberId()));

        // Member 엔티티에 RefreshToken 값을 저장합니다.
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        Gson gson = new Gson();

        Map<String, String> keyMap = Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}