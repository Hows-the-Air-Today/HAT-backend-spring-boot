package io.howstheairtoday.communitydomainrds.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.howstheairtoday.communitydomainrds.entity.Like;
import io.howstheairtoday.communitydomainrds.entity.Post;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {LikeRepositoryTest.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.communitydomainrds.repository")
@EntityScan("io.howstheairtoday.communitydomainrds.entity")
public class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder().region("동남로").content("게시글 내용").build();
        postRepository.save(post);
    }

    @DisplayName("좋아요 등록")
    @Test
    public void insertLike() {

        //given
        Like liked = Like.builder()
            .memberId(UUID.randomUUID())
            .post(post)
            .liked(true)
            .build();

        //when
        Like saveliked = likeRepository.save(liked);

        //then
        assertThat(saveliked).isEqualTo(liked);
    }

    @DisplayName("좋아요 삭제")
    @Test
    public void deleteLiked() {

        //given
        Like liked = Like.builder()
            .memberId(UUID.randomUUID())
            .post(post)
            .liked(true)
            .build();
        likeRepository.save(liked);

        //when
        Like updateLiked = liked.deleteLiked(false);
        Like saveLiked = likeRepository.save(updateLiked);

        //then
        assertThat(saveLiked.getLiked()).isEqualTo(false);
    }
}

