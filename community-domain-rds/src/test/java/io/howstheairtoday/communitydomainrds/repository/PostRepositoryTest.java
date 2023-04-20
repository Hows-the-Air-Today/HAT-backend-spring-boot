package io.howstheairtoday.communitydomainrds.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.querydsl.PostQslImpl;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {PostRepositoryTest.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.communitydomainrds.repository")
@EntityScan("io.howstheairtoday.communitydomainrds.entity")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Mock
    private PostQslRepository postQslRepository;

    //게시물 삽입을 할때 테스트 URL 을 3개를 넣고 작성
    @DisplayName("게시물 삽입")
    @Test
    public void savePost() {
        //given

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        Post post = Post.builder().region("동남로").content("게시글 내용").build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .memeberId(post.getMemberId())
                .post(post)
                .postImageUrl("https://amazons3.com/kjh" + i)
                .build();

            post.insertImages(postImage);

        }

        //when
        Post savePost = postRepository.save(post);

        assertSame(post, savePost);

        Assertions.assertThat(savePost.getContent()).isEqualTo("게시글 내용");
        Assertions.assertThat(savePost.getRegion()).isEqualTo("동남로");
        Assertions.assertThat(savePost.getImageArray().get(0).getPostImageNumber()).isEqualTo(0);

    }

    @DisplayName("게시물 업데이트")
    @Test
    public void updatePost() {

        Post post = Post.builder().region("게시글 업데이트 하기 전 지역").content("게시글 업데이트 하기 전 내용").build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .post(post)
                .postImageUrl("https://amazo3.com/kjh" + i)
                .build();

            post.insertImages(postImage);
        }
        //given
        Post savePost = postRepository.save(post);

        List<PostImage> postImages = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            PostImage postImage = PostImage.builder()
                .postImageNumber(i + 1)
                .post(post)
                .postImageUrl("https://업데이트.com/kjh" + i)
                .build();
            postImages.add(postImage);
        }

        //when
        savePost.updatePost("게시글 내용이 업데이트", "게시글 지역이 업데이트", postImages);

        postRepository.save(savePost);

        //then
        Assertions.assertThat("게시글 업데이트 하기 전 내용").isNotEqualTo(savePost.getContent());
        Assertions.assertThat("게시글 업데이트 하기 전 지역").isNotEqualTo(savePost.getRegion());
        Assertions.assertThat(savePost.getImageArray().size()).isEqualTo(2);

    }

    /**
     * 임의 의 uuid생성 후 게시물을 저장 후 삭제한다
     */
    @DisplayName("게시물 삭제")
    @Test
    public void deletePost() {

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845983");

        Post post = Post.builder().region("게시글 삭제 시작 지역")
            .id(uuid)
            .content("게시글 삭제 시작 내용").build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .post(post)
                .postImageUrl("https://amazo3.com/kjh" + i)
                .build();

            post.insertImages(postImage);
        }
        Post savePost = postRepository.save(post);

        savePost.deletePost();

        Assertions.assertThat(savePost.getDeletedAt()).isNotNull();
        System.out.println(savePost.getDeletedAt());
        Assertions.assertThat(savePost.getDeletedAt()).isEqualTo(savePost.getDeletedAt());

    }

    @DisplayName("게시물 상세 조회")
    @Test
    public void getDetailPost() {

        UUID memberId = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        Post post = Post.builder()
            .memberId(memberId)
            .memberNickname("닉네임입니다")
            .region("게시글 상세를 조회")
            .content("게시글 상세를 조회")
            .build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .memeberId(memberId)
                .post(post)
                .postImageUrl("https://amazo3.com/kjh" + i)
                .build();

            post.insertImages(postImage);
        }
        postRepository.save(post);

        //given
        Optional<Post> getDetailPost = postRepository.findById(post.getId());
        //then
        System.out.println();
        Assertions.assertThat(getDetailPost.get().getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(getDetailPost.get().getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(getDetailPost.get().getRegion()).isEqualTo(post.getRegion());
    }

    @Test
    @DisplayName("자기가 쓴 게시글을 조회한다")
    public void getMyPost() {

        UUID memberId = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845980");

        Post post = Post.builder()
            .memberId(memberId)
            .region("자기가 쓴 게시글 조회하기")
            .content("자기가 쓴 게시글 조회하기")
            .build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .memeberId(memberId)
                .post(post)
                .postImageUrl("https://amazo3.com/kjh" + i)
                .build();

            post.insertImages(postImage);
        }
        postRepository.save(post);

        List<PostImage> getMyPost = postRepository.findByMemberIdAndDeletedAtIsNull(memberId);

        Assertions.assertThat(getMyPost).isNotEmpty();
        Assertions.assertThat(getMyPost.get(0).getMemberId()).isEqualTo(memberId);
    }

    @DisplayName("게시물 조회")
    @Test
    public void getPost() {

        // given
        String region = "서초구";
        int page = 0;
        int limit = 20;

        for (int i = 0; i < 5; i++) {
            Post post = Post.builder()
                .region(region)
                .memberNickname("멤버닉네임")
                .content("게시글 내용" + i)
                .build();

            for (int j = 0; j < 2; j++) {
                PostImage postImage = PostImage.builder()
                    .postImageNumber(j)
                    .post(post)
                    .postImageUrl("https://amazons3.com/kjh" + j)
                    .build();

                post.insertImages(postImage);
            }

            postRepository.save(post);
        }

        //when
        List<Map<String, Object>> posts = postQslRepository.findByRegionList(region, null, 10);

        //then
        Assertions.assertThat(posts).isNotNull();
    }
}
