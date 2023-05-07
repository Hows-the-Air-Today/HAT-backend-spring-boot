package io.howstheairtoday.appcommunityexternalapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import io.howstheairtoday.util.JwtUtil;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private DomainCommunityService domainCommunityService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    private UUID PostUUID;
    private PostRequestDto.SaveRequestDto saveRequestDto;

    private static final String API_V1_POST = "/api/v1/post";
    private static final UUID member_uuid = UUID.fromString("0005cde5-df2b-4d1b-92d7-3f2251c0d5a9");

    @BeforeEach
    public void setJwtToken() {
        Map<String, Object> claimMap = Map.of("loginId", "test");

        jwtToken = jwtUtil.generateToken(claimMap, 1, "");
    }

    @BeforeEach
    public void getPostUUID() {

        saveRequestDto = PostRequestDto.SaveRequestDto.builder()
            .region("beforeEach 지역1")
            .memberImageUrl("beforeEach memberImageUrl")
            .content("beforeEach content")
            .memberNickname("멤버닉네임")
            .memberId(member_uuid)
            .build();

        final Post post = Post.createPost(saveRequestDto.getContent(),
            saveRequestDto.getRegion(), saveRequestDto.getMemberId(), saveRequestDto.getMemberNickname(),
            saveRequestDto.getMemberImageUrl());

        post.insertImages(
            PostImage.create("url",
                post, post.getMemberId()));

        domainCommunityService.savePost(post);

        PostUUID = post.getId();

    }

    @DisplayName("게시글작성에 성공")
    @Test
    public void CreatePost_Success() throws Exception {

        final String fileName = "test.jpeg";
        File newFile = new File(getClass().getClassLoader().getResource("qr.jpeg").getFile());

        MockMultipartFile file = new MockMultipartFile("file", fileName, MediaType.IMAGE_JPEG_VALUE,
            new FileInputStream(newFile));

        List<MultipartFile> postImagesDto = new ArrayList<>();

        postImagesDto.add(file);

        String json = new ObjectMapper().writeValueAsString(saveRequestDto);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.multipart(API_V1_POST + "/create-post")
                .file("postImagesDto", postImagesDto.get(0).getBytes())
                .file("saveRequestDto", json.getBytes())
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));
    }

    @DisplayName("생성된 게시글을 삭제")
    @Test
    public void DeletePost_Success() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete(API_V1_POST + "/" + PostUUID)
                    .header("Authorization", "Bearer " + jwtToken))

            .andExpect(status().isOk())

            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));
    }

    @DisplayName("생성된 게시글의 상세")
    @Test
    public void DetailPost_Success() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_V1_POST + "/" + "post-detail/" + PostUUID)
                    .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
            .andDo(MockMvcResultHandlers.print());

    }

    @DisplayName("게시글 전체 조회")
    @Test
    public void getPost_Success() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_V1_POST + "/")
                    .header("Authorization", "Bearer " + jwtToken)
                    .param("region", "beforeEach 지역1")
                    .param("limit", "10")
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
            .andDo(MockMvcResultHandlers.print());

    }

    @DisplayName("게시글 지역에 따른 인기글 조회")
    @Test
    public void getPopularList_Success() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_V1_POST + "/get-popular")
                    .header("Authorization", "Bearer " + jwtToken)
                    .param("region", "beforeEach 지역1")
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
            .andDo(MockMvcResultHandlers.print());

    }

    @DisplayName("내가 작성한 게시글을 조회")
    @Test
    public void getMyPost_Success() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(API_V1_POST + "/my-page/" + member_uuid)
                    .header("Authorization", "Bearer " + jwtToken)
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
            .andDo(MockMvcResultHandlers.print());

    }
}
