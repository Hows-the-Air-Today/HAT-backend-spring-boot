package io.howstheairtoday.appcommunityexternalapi.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.CommentService;
import io.howstheairtoday.communitydomainrds.dto.CommentPageListDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    //게시물 댓글 작성
    @PostMapping("/{postId}/comments")
    public ApiResponse<Object> createComment(@PathVariable("postId") UUID postId, @RequestBody CommentRequestDTO commentRequestDTO) {

        commentService.createComment(postId, commentRequestDTO);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("작성 성공했습니다.")
            .build();
    }

    //게시물 댓글 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<CommentPageListDTO> getComment(@PathVariable("postId") UUID postId, Integer page) {

        CommentPageListDTO comments = commentService.getComment(postId, page);

        return ApiResponse.<CommentPageListDTO>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("조회 성공했습니다.")
            .data(comments)
            .build();
    }

    //게시물 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ApiResponse<Object> updateComment(@PathVariable("commentId") UUID commentId, @RequestBody CommentRequestDTO commentRequestDTO) {

        commentService.updateComment(commentId, commentRequestDTO);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("수정 성공했습니다.")
            .build();
    }

    //게시물 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ApiResponse<Object> deleteComment(@PathVariable("commentId") UUID commentId) {

        commentService.deleteComment(commentId);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("삭제 성공했습니다.")
            .build();
    }
}
