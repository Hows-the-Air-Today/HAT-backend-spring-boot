package io.howstheairtoday.appcommunityexternalapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.howstheairtoday.appcommunityexternalapi.common.AbstractRestDocsTests;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest extends AbstractRestDocsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID postId;

    private UUID commentId;

    private UUID memberId;

    private Comment comment;

    private Comment savedComment;

    @Autowired
    private DomainCommunityService domainCommunityService;

    @BeforeEach
    void setUp() {

        postId = UUID.randomUUID();
        memberId = UUID.randomUUID();

        comment = Comment.builder()
                .content("작성 댓글")
                .memberId(memberId)
                .build();

        savedComment = domainCommunityService.saveComment(comment);

        commentId = savedComment.getCommentId();

    }


    @DisplayName("댓글 작성 컨트롤러 테스트")
    @Test
    void createComment() throws Exception {

        //given
        Comment comment2 = Comment.builder()
                .content("작성 댓글")
                .memberId(memberId)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/post/{postId}/comments", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment2)));

        // Then
        resultActions.andDo(print());
    }

    @DisplayName("댓글 조회 컨트롤러 테스트")
    @Test
    void getComment() throws Exception {

        //given
        Integer page = 0;

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/post/{postId}/comments", postId)
                .param("page", page.toString())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andDo(print());
    }

    @DisplayName("댓글 수정 컨트롤러 테스트")
    @Test
    void updateComment() throws Exception {

        //given
        comment.updateContent("수정");

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/post/" + postId + "/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));

        // Then
        resultActions.andDo(print());
    }

    @DisplayName("댓글 삭제 컨트롤러 테스트")
    @Test
    void deletedComment() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/post/" + postId + "/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));

        // Then
        resultActions.andDo(print());
    }

}