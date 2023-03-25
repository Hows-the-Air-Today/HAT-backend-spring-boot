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

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final ExternalPostService externalPostService;

    @PostMapping("/post")
    public ApiResponse<Object> createFeed(@Valid @RequestBody PostRequestDto.SaveRequestDto saveRequestDto) {
        externalPostService.createPost(saveRequestDto);
        return ApiResponse.res(HttpStatus.CREATED.value(), "success");
    }
}
