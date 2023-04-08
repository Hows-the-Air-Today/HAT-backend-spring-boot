package io.howstheairtoday.memberappexternalapi.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignUpRequestDTO signUpRequestDto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        signUpRequestDto = new SignUpRequestDTO();
        signUpRequestDto.setLoginId("testId");
        signUpRequestDto.setLoginPassword("testPassword");
        signUpRequestDto.setLoginPasswordCheck("testPassword");
        signUpRequestDto.setEmail("test@test.com");
        signUpRequestDto.setNickname("테스트닉네임");
    }

    @DisplayName("회원가입 - 성공")
    @Test
    void registerTest() {

        // given
        doReturn(false).when(memberRepository).existsByLoginId(any());
        doReturn(false).when(memberRepository).existsByNickname(any());
        doReturn("encodePassword").when(passwordEncoder).encode(any());
        doReturn(new Member()).when(memberRepository).save(any(Member.class));

        // when
        ApiResponse<?> apiResponse = authService.register(signUpRequestDto);

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), apiResponse.getStatusCode());
    }
}
