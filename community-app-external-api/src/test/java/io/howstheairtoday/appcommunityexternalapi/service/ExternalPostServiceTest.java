package io.howstheairtoday.appcommunityexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import io.howstheairtoday.appcommunityexternalapi.exception.posts.PostNotExistException;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.repository.PostRepository;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;

@ActiveProfiles("test")
@SpringBootTest
public class ExternalPostServiceTest {

    @Autowired
    private DomainCommunityService domainCommunityService;

    @Autowired
    private ExternalPostService externalPostService;

    /*
     * domainCommunityService에 Post를 Entity를 넘기고 성공적으로 데이터베이스에 삽입 성공
     */
    @DisplayName("게시물 삽입")
    @Test
    public void createPost() {

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        List<PostRequestDto.PostImagesDto> postImagesList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            PostRequestDto.PostImagesDto postImagesDto = PostRequestDto.PostImagesDto.builder()
                .postImageNumber(i)
                // .postImageUrl("https://amazon.com")
                .build();
            postImagesList.add(postImagesDto);
        }

        //given
        PostRequestDto.SaveRequestDto postRequestDto = PostRequestDto.SaveRequestDto.builder()
            .content("안녕하세요")
            .memberId(uuid)
            // .postImageDtoList(postImagesList)
            .region("가락동")
            .build();

        Post post = Post.createPost(postRequestDto.getContent(),
            postRequestDto.getRegion(), postRequestDto.getMemberId());

        post.getImageArray()
            .forEach(postImage -> post.insertImages(
                PostImage.create(postImage.getPostImageNumber(), postImage.getPostImageUrl(), post,
                    post.getMemberId())));

        //when
        domainCommunityService.savePost(post);

        //then
        assertNotNull(post);

    }

    @DisplayName("게시물 업데이트")
    @Test
    public void updatePostService() {

        //given
        Post post = Post.builder().region("동남로").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        domainCommunityService.savePost(post);

        assertThat(post.getContent()).isEqualTo("게시글 내용");
        assertThat(post.getRegion()).isEqualTo("동남로");
        assertThat(post.getImageArray().get(0).getPostImageNumber()).isEqualTo(1);
        assertThat(post.getImageArray().get(0).getPostImageUrl()).isEqualTo("https://amazons3.com/kjh");

        //when
        final List<PostImage> postImageList = new ArrayList<>();

        post.getImageArray().forEach(postImageDto -> {
            PostImage postImages = PostImage.builder()
                .postImageUrl("업데이트 된 url !")
                .postImageNumber(3)
                .build();
            postImageList.add(postImages);
        });

        post.updatePost("업데이트", "지역도업데이트", postImageList);

        //then
        assertThat(post.getContent()).isEqualTo("업데이트");
        assertThat(post.getRegion()).isEqualTo("지역도업데이트");
        assertThat(post.getImageArray().get(0).getPostImageUrl()).isEqualTo("업데이트 된 url !");
        assertThat(post.getImageArray().get(0).getPostImageNumber()).isEqualTo(3);

    }

    @DisplayName("게시물 삭제")
    @Test()
    public void deletePost() {
        //given
        Post post = Post.builder().region("동남로").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        Assertions.assertDoesNotThrow(() -> domainCommunityService.savePost(post));

        post.deletePost();

        Assertions.assertNotNull(post.getDeletedAt());

    }

    @DisplayName("게시물 업로드 ")
    @Test()
    public void createWithImageUploadPost() throws IOException {
        //given
        PostRequestDto.SaveRequestDto saveRequestDto = PostRequestDto.SaveRequestDto.builder()
            .content("e")
            .region("#4")
            .memberId(UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987"))
            .build();

        List<PostRequestDto.PostImagesDto> postImagesDtos = new ArrayList<>();

        File newFile = new File(getClass().getClassLoader().getResource("qr.jpeg").getFile());

        MockMultipartFile file = new MockMultipartFile("image/jpeg",
            new FileInputStream(
                newFile));
        PostRequestDto.PostImagesDto postImagesDto = PostRequestDto.PostImagesDto.builder()
            .postImageNumber(1)
            .postImageUrl(file)
            .build();

        postImagesDtos.add(postImagesDto);

        externalPostService.createPost(saveRequestDto, postImagesDtos);
    }

    @DisplayName("게시물 상세")
    @Test
    public void getDetailServicePosts() {

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");
        Post post = Post.builder().memberId(uuid).region("동남로").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        domainCommunityService.savePost(post);

        //given
        Post getPost = domainCommunityService.findById(post.getId()).orElseThrow(PostNotExistException::new);

        //when
        PostResponseDto.PostResponseDetail postResponseDetail = externalPostService.getDetailPost(getPost.getId());

        //then
        assertThat(getPost.getContent()).isEqualTo("게시글 내용");
        assertThat(getPost.getRegion()).isEqualTo("동남로");
        assertThat(getPost.getId()).isEqualTo(postResponseDetail.getPostDto().getPostId());
    }

    @Test
    @DisplayName("나의 게시글을 조회한다")
    public void getMyPost() {
        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845981");
        Post post = Post.builder().memberId(uuid).region("동남로").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .memeberId(uuid)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        domainCommunityService.savePost(post);

        Post getPost = domainCommunityService.findById(post.getId()).orElseThrow(PostNotExistException::new);

        //given
        List<PostResponseDto.PostImageDto> dto = externalPostService.getMyPost(getPost.getMemberId());

        assertThat(dto).isNotNull();
    }

}
