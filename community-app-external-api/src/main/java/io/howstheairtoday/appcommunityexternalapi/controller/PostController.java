package io.howstheairtoday.appcommunityexternalapi.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.howstheairtoday.appcommunityexternalapi.common.ApiResponse;
import io.howstheairtoday.appcommunityexternalapi.exception.posts.PostNotMember;
import io.howstheairtoday.appcommunityexternalapi.service.ExternalPostService;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostExternalDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
import io.howstheairtoday.communitydomainrds.dto.DomainPostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 게시글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostController {
    private final ExternalPostService externalPostService;

    /**
     * 게시글을 생성
     *
     * @param saveRequestDto 생성할 게시글 정보
     * @return ApiResponse
     */
    @PostMapping(path = "/create-post")
    public ApiResponse<Object> createPost(
        @RequestPart("saveRequestDto") final PostRequestDto.SaveRequestDto saveRequestDto,
        @RequestPart("postImagesDto") final List<MultipartFile> postImagesDto) {
        externalPostService.createPost(saveRequestDto, postImagesDto);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("success")
            .build();
    }

    @PatchMapping("/{postsId}")
    public ApiResponse<Object> updatePost(
        @RequestPart("saveRequestDto") final PostRequestDto.SaveRequestDto saveRequestDto,
        @RequestPart("postImagesDto") final List<MultipartFile> postImagesDto,
        @PathVariable UUID postsId
    ) {
        externalPostService.updatePost(saveRequestDto, postsId, postImagesDto);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .build();
    }

    @DeleteMapping("/{postsId}")
    public ApiResponse<Object> deletePost(@Valid @PathVariable UUID postsId
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

    @GetMapping("/")
    public ApiResponse<Object> getPost(
        @RequestParam("region") String region,
        @RequestParam(value = "createdAt", required = false) LocalDateTime createdAt,
        // lastId가 없는 경우에는 null로 처리됨
        @RequestParam("limit") int limit
    ) {
        PostExternalDto dto = externalPostService.getPostQsl(region, createdAt, limit);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .data(dto)
            .build();
    }

    @GetMapping("my-page/{memberId}")
    public ApiResponse<Object> getDetailMyPagePost(@PathVariable UUID memberId
    ) {
        List<PostResponseDto.PostImageDto> domainPostResponseDtos = externalPostService.getMyPost(memberId);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .data(domainPostResponseDtos)
            .build();
    }
}
