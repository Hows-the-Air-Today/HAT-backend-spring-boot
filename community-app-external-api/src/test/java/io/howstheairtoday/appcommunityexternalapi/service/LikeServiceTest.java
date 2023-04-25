package io.howstheairtoday.appcommunityexternalapi.service;

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
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;

@ActiveProfiles("test")
@SpringBootTest
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    private LikeRequestDTO likeRequestDTO;

    @Autowired
    private DomainCommunityService domainCommunityService;

    private Post post;

    @BeforeEach
    void setUp() {

        //given
        post = Post.builder().region("서초구").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        domainCommunityService.savePost(post);
    }

    @DisplayName("좋아요 등록 및 삭제")
    @Test
    public void createLike(){


        likeRequestDTO = likeRequestDTO.builder()
            .memberId(UUID.randomUUID())
            .build();

        LikeResponseDTO savedLike = likeService.createLike(post, likeRequestDTO);

        //then
        assertNotNull(savedLike);
    }
}