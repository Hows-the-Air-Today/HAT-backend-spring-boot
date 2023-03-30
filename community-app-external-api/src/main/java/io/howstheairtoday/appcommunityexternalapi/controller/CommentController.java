package io.howstheairtoday.appcommunityexternalapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.CommentService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
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
            .msg("성공했습니다.")
            .build();
    }
}
