package io.howstheairtoday.appcommunityexternalapi.service.dto.request;

import java.util.List;
import java.util.UUID;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        @NotBlank(message = "지역을 입력해주세요.")
        private String location;

        @NotNull(message = "유저아이디가 존재하지 않습니다.")
        private UUID userId;

        private List<PostImagesDto> postImageDtoList;

        public void updatePost(String content, String location) {
            this.content = content;
            this.location = location;

        }
    }

    @Getter
    @Builder
    public static class PostImagesDto {

        private String postImageUrl;

        private Integer postImageNumber;

    }

}