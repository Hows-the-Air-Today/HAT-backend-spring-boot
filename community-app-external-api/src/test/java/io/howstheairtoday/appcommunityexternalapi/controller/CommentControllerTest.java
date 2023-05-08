package io.howstheairtoday.appcommunityexternalapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.howstheairtoday.appcommunityexternalapi.common.AbstractRestDocsTests;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import io.howstheairtoday.util.JwtUtil;

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

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest extends AbstractRestDocsTests {

    @Autowired
    private DomainCommunityService domainCommunityService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    private UUID postId;

    private UUID commentId;

    private Comment comment;

    private Comment savedComment;

    private PostRequestDto.SaveRequestDto saveRequestDto;

    private static final UUID memberId = UUID.fromString("0005cde5-df2b-4d1b-92d7-3f2251c0d5a9");

    @BeforeEach
    public void setJwtToken() {

        Map<String, Object> claimMap = Map.of("loginId", "test");

        jwtToken = jwtUtil.generateToken(claimMap, 1, "");
    }

    @BeforeEach
    public void getPostUUID() {

        saveRequestDto = PostRequestDto.SaveRequestDto.builder()
            .region("지역1")
            .memberImageUrl("memberImageUrl")
            .content("content")
            .memberNickname("닉네임")
            .memberId(memberId)
            .build();

        final Post post = Post.createPost(saveRequestDto.getContent(),
            saveRequestDto.getRegion(), saveRequestDto.getMemberId(), saveRequestDto.getMemberNickname(),
            saveRequestDto.getMemberImageUrl());

        post.insertImages(
            PostImage.create("url",
                post, post.getMemberId()));

        domainCommunityService.savePost(post);

        postId = post.getId();

    }

    @BeforeEach
    public void getCommentUUID() {

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
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment2)));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("댓글 조회 컨트롤러 테스트")
    @Test
    void getComment() throws Exception {

        //given
        Integer page = 0;

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/post/{postId}/comments", postId)
                .header("Authorization", "Bearer " + jwtToken)
                .param("page", page.toString())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("댓글 수정 컨트롤러 테스트")
    @Test
    void updateComment() throws Exception {

        //given
        comment.updateContent("수정");

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/post/" + postId + "/comments/{commentId}", commentId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("댓글 삭제 컨트롤러 테스트")
    @Test
    void deletedComment() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/post/" + postId + "/comments/{commentId}", commentId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

}