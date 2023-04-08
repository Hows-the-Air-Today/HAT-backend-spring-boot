package io.howstheairtoday.appcommunityexternalapi.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID postId;

    private UUID commentId;

    private UUID memberId;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        memberId = UUID.randomUUID();
        commentId = UUID.fromString("f8c169df-17f9-4994-b587-9c113cec384f");
    }


    @DisplayName("댓글 작성 컨트롤러 테스트")
    @Test
    void createComment() throws Exception {

        //given
        CommentRequestDTO requestDTO = new CommentRequestDTO("test comment", memberId);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/post/{postId}/comments", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // Then
        resultActions.andDo(print());
    }

    @DisplayName("댓글 수정 컨트롤러 테스트")
    @Test
    void modifyComment() throws Exception{

        //given
        CommentRequestDTO requestDTO = new CommentRequestDTO("test comment update", memberId);

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/post/12348e8-87b9-4f57-a651-4462412344bf/comments/{commentId}", commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)));

        // Then
        resultActions.andDo(print());
    }

}