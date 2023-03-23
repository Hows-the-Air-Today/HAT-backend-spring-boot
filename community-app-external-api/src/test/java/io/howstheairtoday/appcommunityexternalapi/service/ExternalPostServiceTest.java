package io.howstheairtoday.appcommunityexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
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

    @DisplayName("게시물 작성에 성공")
    @Test
    public void createPost() {

        List<PostImage> postImagesList = new ArrayList<>();
        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        //given

        PostRequestDto.SaveRequestDto postRequestDto = PostRequestDto.SaveRequestDto.builder()
            .content("안녕하세요")
            .userId(uuid)
            .region("가락동")
            .postImageDtoList(postImagesList)
            .build();

        Post post = postRequestDto.toEntity();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageUrl("https://www.amazon.com/" + i)
                .postImageNumber(i)
                .post(post)
                .build();
            postImagesList.add(postImage);
        }

        //when
        Post savePost = domainCommunityService.savePost(post);

        //then
        assertSame(post, savePost);

        Assertions.assertThat(savePost).isNotNull();

    }

}
