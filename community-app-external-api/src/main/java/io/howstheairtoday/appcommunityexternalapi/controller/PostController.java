package io.howstheairtoday.appcommunityexternalapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.service.ExternalPostService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
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
    @PostMapping("/post")
    public ApiResponse<Object> createPost(@Valid @RequestBody final PostRequestDto.SaveRequestDto saveRequestDto) {
        externalPostService.createPost(saveRequestDto);
        return ApiResponse.res(HttpStatus.CREATED.value(), "success");
    }
}
