package io.howstheairtoday.memberappexternalapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.howstheairtoday.memberappexternalapi.common.AbstractRestDocsTests;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;

@DisplayName("회원 API 테스트")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/auth";
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void deletData() {

        Optional<Member> member = memberRepository.findByLoginId("test");
        if (member != null) {
            memberRepository.delete(member.get());
        }
    }

    @DisplayName("회원가입")
    @Test
    public void registerMemberTest() throws Exception {

        // given
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setLoginId("test");
        signUpRequestDTO.setLoginPassword("test123");
        signUpRequestDTO.setLoginPasswordCheck("test123");
        signUpRequestDTO.setEmail("test@test.com");
        signUpRequestDTO.setNickname("테스트");

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDTO))
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("회원 정보 조회")
    @Test
    public void readMemberTest() throws Exception {

        // given
        Member member = Member.builder()
            .loginId("test")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("테스트")
            .memberProfileImage("default.jpg")
            .loginRole(LoginRole.ROLE_USER)
            .build();
        memberRepository.save(member);

        String accessToken = "example_access_token";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + "/" + member.getMemberId())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}