package io.howstheairtoday.communitydomainrds.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
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

import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {CommentRepositoryTest.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.communitydomainrds.repository")
@EntityScan("io.howstheairtoday.communitydomainrds.entity")
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder().region("동남로").content("게시글 내용").build();
        postRepository.save(post);
    }
    @DisplayName("댓글 작성")
    @Test
    public void insertComment() {

        //given
        Comment comment = Comment.builder()
            .memberId(UUID.randomUUID())
            .content("댓글3")
            .post(post)
            .build();

        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        assertThat(savedComment).isEqualTo(comment);
    }

    @DisplayName("댓글 조회")
    @Test
    public void getComment() {

        //given
        Comment comment2 = Comment.builder()
            .memberId(UUID.randomUUID())
            .content("댓글6")
            .post(post)
            .build();

        commentRepository.save(comment2);

        //when
        Optional<Comment> getComment =
            commentRepository.findById(comment2.getCommentId());

        //then
        assertThat(getComment.get().getContent()).isEqualTo("댓글6");
    }

    @DisplayName("댓글 수정")
    @Test
    public void modifyComment() {

        //given
        Comment comment1 = Comment.builder()
            .memberId(UUID.randomUUID())
            .content("댓글4")
            .post(post)
            .build();
        commentRepository.save(comment1);

        //when
        Comment changecomment = comment1.updateContent("댓글 수정");
        Comment savedComment = commentRepository.save(changecomment);

        //then
        assertThat(savedComment.getContent()).isEqualTo("댓글 수정");
    }

    @DisplayName("댓글 삭제")
    @Test
    public void deleteComment() {

        //given
        Comment comment = Comment.builder()
            .memberId(UUID.randomUUID())
            .content("댓글15")
            .post(post)
            .build();

        //when
        commentRepository.save(comment);
        commentRepository.delete(comment);

        //then
        assertFalse(commentRepository.existsById(comment.getCommentId()));
    }
}

