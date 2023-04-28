package io.howstheairtoday.appcommunityexternalapi.service.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import jakarta.annotation.security.DenyAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostResponseDto {

    @Builder
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @Getter
    public static class PostResponseDetail {
        private PostDto postDto;
        private List<PostResponseDto.PostImageResponseDto> imageDto;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostDto {
        private UUID postId;
        private UUID memberId;
        private String content;
        private String memberNickname;
        private String region;
        private String imageUrl;

        private String memberImageUrl;
        private int commentCount;
        private int likeCount;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

    }

    @Getter
    @Builder
    public static class PostImageResponseDto {

        private UUID imageId;
        private UUID memberId;
        private String content;

        private UUID postId;
        private String imageUrl;
        private Integer imageNumber;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class PostImageDto {
        private String postImageUrl;
        private UUID memberId;
        private UUID postId;

    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class PopularList {
        // private final List<> popularList;
        private UUID postId;
        private UUID memberId;
        private String content;
        private String memberNickname;
        private String region;
        private String imageUrl;


    }
}

