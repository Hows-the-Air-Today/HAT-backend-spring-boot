package io.howstheairtoday.appcommunityexternalapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;

@ActiveProfiles("test")
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 작성")
    @Test
    public void createCommentTest(){

        //given
        UUID postId = UUID.randomUUID();
        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
            .content("테스트 댓글")
            .memberId(UUID.randomUUID())
            .build();

        //when
        commentService.createComment(postId, commentRequestDTO);

        //then
        assertNotNull(postId);
        assertNotNull(commentRequestDTO.getContent());
        assertNotNull(commentRequestDTO.getMemberId());
    }

    @DisplayName("댓글 수정")
    @Test
    void updateComment() {

        // given
        UUID postId = UUID.randomUUID();

        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
            .content("테스트 댓글2")
            .memberId(UUID.randomUUID())
            .build();

        Comment savedComment = commentService.createComment(postId, commentRequestDTO);

        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
            .content("수정된 댓글")
            .build();

        // when
        Comment updatedComment = commentService.updateComment(savedComment.getCommentId(), requestDTO);

        // then
        assertThat(updatedComment.getContent()).isEqualTo("수정된 댓글");
    }
}
