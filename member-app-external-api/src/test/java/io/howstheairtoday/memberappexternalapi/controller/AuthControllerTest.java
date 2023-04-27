package io.howstheairtoday.memberappexternalapi.controller;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.howstheairtoday.memberappexternalapi.common.AbstractRestDocsTests;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;

@DisplayName("회원 API 테스트")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/auth";

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
}