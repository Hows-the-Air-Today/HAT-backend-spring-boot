package io.howstheairtoday.memberappexternalapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.memberappexternalapi.service.dto.request.MemberRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ExternalMemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExternalMemberService externalMemberService;

    // @AfterEach
    // public void clean() {
    //     memberRepository.deleteAll();
    // }

    @DisplayName("회원 생성 기능 - 기본")
    @Test
    void createMemberTest() throws Exception {
        // given
        MemberRequestDTO.SaveMemberRequestDto requestDTO = MemberRequestDTO.SaveMemberRequestDto.builder()
            .loginId("hat")
            .loginPassword("test123")
            .email("hat@test.com")
            .nickname("hat")
            .build();

        // when
        UUID memberId = externalMemberService.createMember(requestDTO);

        // then
        Member savedMember = memberRepository.findById(memberId).orElseThrow(Exception::new);
        assertThat(savedMember.getLoginId()).isEqualTo(requestDTO.getLoginId());
        assertThat(savedMember.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(savedMember.getNickname()).isEqualTo(requestDTO.getNickname());
        assertThat(savedMember.getMemberProfileImage()).isEqualTo("default.jpg");
        assertThat(savedMember.getLoginType()).isEqualTo(LoginType.LOCAL);
        assertThat(savedMember.getLoginRole()).isEqualTo(LoginRole.ROLE_USER);
        assertThat(passwordEncoder.matches(requestDTO.getLoginPassword(), savedMember.getLoginPassword())).isTrue();
    }
}
