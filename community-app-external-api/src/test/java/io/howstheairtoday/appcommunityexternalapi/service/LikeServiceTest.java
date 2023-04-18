package io.howstheairtoday.appcommunityexternalapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.LikeRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.LikeResponseDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Like;

@ActiveProfiles("test")
@SpringBootTest
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    private LikeRequestDTO likeRequestDTO;

    private UUID postId;

    @DisplayName("좋아요 등록 및 삭제")
    @Test
    public void createLike(){

        postId = UUID.randomUUID();

        likeRequestDTO = likeRequestDTO.builder()
            .memberId(UUID.randomUUID())
            .build();

        LikeResponseDTO savedLike = likeService.createLike(postId, likeRequestDTO);

        //then
        assertNotNull(savedLike);
    }
}