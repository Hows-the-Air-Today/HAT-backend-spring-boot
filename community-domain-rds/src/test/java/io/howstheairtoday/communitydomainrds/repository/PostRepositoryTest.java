package io.howstheairtoday.communitydomainrds.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시물 삽입을 할때 테스트 URL을 3개를 넣고 작성")
    @Test
    public void savePost() {
        //given
        List<PostImage> list = new ArrayList<>();

        UUID uuid = UUID.fromString("dc718b8f-fb97-48d4-b55d-855e7c845987");

        Post post = Post.builder().region("동남로").userId(uuid).imageArray(list).content("게시글 내용").build();

        for (int i = 0; i < 3; i++) {
            PostImage postImage = PostImage.builder()
                .postImageNumber(i)
                .post(post)
                .postImageUrl("https://amazons3.com/kjh" + i)
                .build();

            list.add(postImage);
        }

        //when
        Post savePost = postRepository.save(post);

        //then
        assertSame(post, savePost);
        Assertions.assertThat(savePost.getContent()).isEqualTo("게시글 내용");
        Assertions.assertThat(savePost.getRegion()).isEqualTo("동남로");
        Assertions.assertThat(savePost.getImageArray().get(0).getPostImageNumber()).isEqualTo(0);

    }

}
