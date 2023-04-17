package io.howstheairtoday.appcommunityexternalapi.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.ExternalPostService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.ExternalPostService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 게시글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final ExternalPostService externalPostService;

    /**
     * 게시글을 생성
     *
     * @param saveRequestDto 생성할 게시글 정보
     * @return ApiResponse
     */
    @PostMapping(path = "/post")
    public ApiResponse<Object> createPost(
        @Valid @RequestPart PostRequestDto.SaveRequestDto saveRequestDto,
        @RequestPart List<PostRequestDto.PostImagesDto> postImages) {

        externalPostService.createPost(saveRequestDto, postImages);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("success")
            .build();
    }

    @PatchMapping("/post/{postsId}")
    public ApiResponse<Object> updatePost(@Valid @RequestPart final PostRequestDto.SaveRequestDto saveRequestDto,
        @RequestPart List<PostRequestDto.PostImagesDto> postImages,
        @PathVariable UUID postsId
    ) {
        externalPostService.updatePost(saveRequestDto, postsId, postImages);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .build();
    }

    @DeleteMapping("/post")
    public ApiResponse<Object> deletePost(@Valid @RequestBody PostRequestDto.PostUUIDDto postsId
    ) {
        externalPostService.deletePost(postsId);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .build();
    }

    @GetMapping("/post-detail")
    public ApiResponse<Object> getDetailPost(@RequestParam UUID postsId
    ) {
        PostResponseDto.PostResponseDetail dto = externalPostService.getDetailPost(postsId);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .data(dto)
            .build();
    }
}
