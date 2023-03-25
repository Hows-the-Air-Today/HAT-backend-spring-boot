package io.howstheairtoday.appcommunityexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;

@ActiveProfiles("test")
@SpringBootTest
public class ExternalPostServiceTest {

    @Autowired
    private DomainCommunityService domainCommunityService;

    @DisplayName("domainCommunityService에 Post를 Entity를 넘기고 성공적으로 데이터베이스에 삽입 성공")
    @Test
    public void createPost() {

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        List<PostRequestDto.PostImagesDto> postImagesList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            PostRequestDto.PostImagesDto postImagesDto = PostRequestDto.PostImagesDto.builder()
                .postImageNumber(i)
                .postImageUrl("https://amazon.com" + i)
                .build();
            postImagesList.add(postImagesDto);
        }

        //given
        PostRequestDto.SaveRequestDto postRequestDto = PostRequestDto.SaveRequestDto.builder()
            .content("안녕하세요")
            .userId(uuid)
            .postImageDtoList(postImagesList)
            .content("가락동")
            .build();

        Post post = Post.createPost(postRequestDto.getContent(), postRequestDto.getLocation());

        post.getImageArray()
            .forEach(postImage -> post.imagesAdd(
                PostImage.createImages(postImage.getPostImageNumber(), postImage.getPostImageUrl(), post)));

        //when
        domainCommunityService.savePost(post);

        //then
        assertNotNull(post);

    }

}
