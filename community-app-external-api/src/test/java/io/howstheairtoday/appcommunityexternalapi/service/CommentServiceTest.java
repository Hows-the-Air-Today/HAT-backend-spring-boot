package io.howstheairtoday.appcommunityexternalapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.CommentResponseDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;

@ActiveProfiles("test")
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    private UUID postId;

    private UUID commentId;

    private UUID memberId;

    private CommentRequestDTO commentRequestDTO;

    private Comment savedComment;

    @BeforeEach
    void setUp() {

        postId = UUID.randomUUID();
        memberId = UUID.randomUUID();

        commentRequestDTO = CommentRequestDTO.builder()
            .content("테스트 댓글")
            .memberId(UUID.randomUUID())
            .build();

        //when
        savedComment = commentService.createComment(postId, commentRequestDTO);
    }

    @DisplayName("댓글 작성")
    @Test
    public void createCommentTest(){

        //then
        assertNotNull(postId);
        assertNotNull(commentRequestDTO.getContent());
        assertNotNull(commentRequestDTO.getMemberId());
    }

    @DisplayName("댓글 조회")
    @Test
    void getComment() {

        //when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Slice<CommentResponseDTO> result = commentService.getComment(postId, pageRequest);

        //then
        assertThat(result.getContent().get(0).getContent()).isEqualTo(commentRequestDTO.getContent());
    }

    @DisplayName("댓글 수정")
    @Test
    void updateComment() {

        //given
        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
            .content("수정된 댓글")
            .build();

        // when
        Comment updatedComment = commentService.updateComment(savedComment.getCommentId(), requestDTO);

        // then
        assertThat(updatedComment.getContent()).isEqualTo("수정된 댓글");
    }

    @DisplayName("댓글 삭제")
    @Test
    void deletedComment() {

        // when
        Comment updatedComment = commentService.deleteComment(savedComment.getCommentId());

        // then
        assertNotNull(updatedComment.getDeletedAt());
    }
}
