package io.howstheairtoday.communitydomainrds.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.UUID;

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

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {LikeRepository.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.communitydomainrds.repository")
@EntityScan("io.howstheairtoday.communitydomainrds.entity")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @DisplayName("좋아요 등록")
    @Test
    public void insertLike() {

        //given
        Like liked = Like.builder()
            .memberId(UUID.randomUUID())
            .postId(UUID.randomUUID())
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
            .postId(UUID.randomUUID())
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

