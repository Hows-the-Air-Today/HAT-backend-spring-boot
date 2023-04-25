package io.howstheairtoday.appcommunityexternalapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.CommentResponseDTO;
import io.howstheairtoday.communitydomainrds.dto.CommentPageListDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;

@ActiveProfiles("test")
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DomainCommunityService domainCommunityService;

    private Post post;

    private UUID memberId;

    private CommentRequestDTO commentRequestDTO;

    private CommentResponseDTO savedComment;

    @BeforeEach
    void setUp() {

        //given
        post = Post.builder().region("서초구").content("게시글 내용").build();

        PostImage postImage = PostImage.builder()
            .postImageNumber(1)
            .post(post)
            .postImageUrl("https://amazons3.com/kjh")
            .build();

        post.insertImages(postImage);

        domainCommunityService.savePost(post);

        memberId = UUID.randomUUID();

        commentRequestDTO = CommentRequestDTO.builder()
            .content("테스트 댓글")
            .memberId(memberId)
            .build();

        //when
        savedComment = commentService.createComment(post, commentRequestDTO);
    }

    @DisplayName("댓글 작성")
    @Test
    public void createCommentTest(){
        //then
        assertNotNull(commentRequestDTO.getContent());
        assertNotNull(commentRequestDTO.getMemberId());
    }

    @DisplayName("댓글 조회")
    @Test
    void getComment() {

        //given
        Integer page = 0;

        //when
        CommentPageListDTO result = commentService.getComment(post, page);

        //then
        assertNotNull(result.getCommentPageDTOList());
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
