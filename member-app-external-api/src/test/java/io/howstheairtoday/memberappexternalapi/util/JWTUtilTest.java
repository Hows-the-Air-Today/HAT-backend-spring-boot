package io.howstheairtoday.memberappexternalapi.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    private String jwtToken;

    @BeforeEach
    public void setJwtToken() {
        // given
        Map<String, Object> claimMap = Map.of("loginId", "test");

        // when
        jwtToken = jwtUtil.generateToken(claimMap, 1);
    }

    @DisplayName("JWT 토큰 생성 설정 확인")
    @Test
    public void testGenerateToken() {
        // give
        // @BeforeEach에서 수행
        // when
        int dotCount = jwtToken.length() - jwtToken.replaceAll("\\.", "").length();

        // then
        assertThat(jwtToken).isNotNull();
        assertTrue(dotCount == 2);
    }

    @DisplayName("JWT 토큰 생성 유효성 검사 - 성공")
    @Test
    public void testValidateSuccess() {
        // when
        Map<String, Object> claim = jwtUtil.validateToken(jwtToken);

        // then
        assertThat(claim).isNotNull();
    }

    /**
     * 유효하지 않은 JWT 토큰을 이용하여 validateToken() 메소드를 실행하였을 때, Error를 발생하는지 검증
     */
    @DisplayName("JWT 토큰 생성 유효성 검사 - 실패")
    @Test
    public void testValidateFail() {
        // given
        String invalidToken = "invalid-jwt-header.payload.signature";

        // then
        assertThrows(MalformedJwtException.class, () -> {
            // when
            jwtUtil.validateToken(invalidToken);
        });
    }
}