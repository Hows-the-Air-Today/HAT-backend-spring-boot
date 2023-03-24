package io.howstheairtoday.communitydomainrds.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {PostRepositoryTest.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.communitydomainrds.repository")
@EntityScan("io.howstheairtoday.communitydomainrds.entity")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시물 삽입을 할때 테스트 URL 을 3개를 넣고 작성")
    @Test
    public void savePost() {
        //given

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        Post post = Post.builder().location("동남로").content("게시글 내용").build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .post(post)
                .postImageUrl("https://amazons3.com/kjh" + i)
                .build();

            post.imagesAdd(postImage);

        }

        //when
        Post savePost = postRepository.save(post);

        assertSame(post, savePost);
        Assertions.assertThat(savePost.getContent()).isEqualTo("게시글 내용");
        Assertions.assertThat(savePost.getLocation()).isEqualTo("동남로");
        Assertions.assertThat(savePost.getImageArray().get(0).getPostImageNumber()).isEqualTo(0);

    }

}
