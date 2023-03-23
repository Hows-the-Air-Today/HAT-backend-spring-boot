package io.howstheairtoday.appcommunityexternalapi.service.dto.request;

import java.util.List;
import java.util.UUID;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class SaveRequestDto {

        @NotBlank(message = "내용을 입력해주세요")
        private String content;

        @NotBlank(message = "지역을 입력해주세요")
        private String region;

        @NotBlank(message = "유저아이디가 존재하지 않습니다.")
        private UUID userId;

        @NotBlank(message = "이미지가 존재하지 않습니다")
        private List<PostImage> postImageDtoList;

        private UUID id;

        public Post toEntity() {
            return Post.builder()
                .userId(this.userId)
                .region(this.region)
                .id(id)
                .imageArray(this.postImageDtoList)
                .content(this.content)
                .build();
        }

        public void updatePost(String content, String region) {
            this.content = content;
            this.region = region;

        }
    }

    @Getter
    @Builder
    public static class PostImageDto {
        private Integer postImageNumber;
        private String postImageUrl;
        private Post post;
        private UUID id;

    }

}