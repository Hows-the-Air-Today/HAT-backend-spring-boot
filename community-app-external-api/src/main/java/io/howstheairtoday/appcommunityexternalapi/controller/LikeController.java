package io.howstheairtoday.appcommunityexternalapi.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.LikeService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.LikeRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.LikeResponseDTO;
import io.howstheairtoday.communitydomainrds.entity.Post;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    public ApiResponse<Object> createLike(@PathVariable("postId") Post postId, @RequestBody LikeRequestDTO likeRequestDTO) {

        LikeResponseDTO like = likeService.createLike(postId, likeRequestDTO);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("성공")
            .data(like)
            .build();
    }
}
