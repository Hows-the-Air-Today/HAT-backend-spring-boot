package io.howstheairtoday.appcommunityexternalapi.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.CommentResponseDTO;
import io.howstheairtoday.communitydomainrds.dto.CommentPageListDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final DomainCommunityService domainCommunityService;

    //게시물 댓글 작성 처리
    public CommentResponseDTO createComment(Post postId, CommentRequestDTO commentRequestDTO){

        Comment comment = Comment.builder()
            .post(postId)
            .content(commentRequestDTO.getContent())
            .memberId(commentRequestDTO.getMemberId())
            .nickName(commentRequestDTO.getNickname())
            .memberProfileImage(commentRequestDTO.getMemberProfileImage())
            .build();

        domainCommunityService.saveComment(comment);

        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .post(postId)
            .memberId(comment.getMemberId())
            .nickName(comment.getNickName())
            .memberProfileImage(comment.getMemberProfileImage())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();

        return commentResponseDTO;
    }

    //게시물 댓글 조회 처리
    public CommentPageListDTO getComment(Post postId, Integer pageable) {

        return domainCommunityService.getComment(postId, pageable);
    }

    //게시물 댓글 수정 처리
    public Comment updateComment(UUID commentId, CommentRequestDTO commentRequestDTO){

        Optional<Comment> comment = domainCommunityService.findCommentId(commentId);
        Comment updatedComment = comment.get();

        if (comment.isPresent()) {

            updatedComment.updateContent(commentRequestDTO.getContent());
            domainCommunityService.saveComment(updatedComment);
        }

        return updatedComment;
    }

    //게시물 댓글 삭제 처리 (softDelete)
    public Comment deleteComment(UUID commentId) {

        Optional<Comment> comment = domainCommunityService.findCommentId(commentId);

        Comment deletedComment = comment.get();
        deletedComment.deletedAt(LocalDateTime.now());

        domainCommunityService.saveComment(deletedComment);

        return deletedComment;
    }
}