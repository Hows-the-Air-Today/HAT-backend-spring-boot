package io.howstheairtoday.appcommunityexternalapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;
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

import io.howstheairtoday.appcommunityexternalapi.common.AbstractRestDocsTests;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.LikeRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import io.howstheairtoday.util.JwtUtil;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class LikeControllerTest extends AbstractRestDocsTests {

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

    @DisplayName("좋아요 컨트롤러 테스트")
    @Test
    void createLike() throws Exception {

        //given
        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
            .memberId(memberId)
            .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/post/{postId}/likes", postId)
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(likeRequestDTO)));

        //then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}