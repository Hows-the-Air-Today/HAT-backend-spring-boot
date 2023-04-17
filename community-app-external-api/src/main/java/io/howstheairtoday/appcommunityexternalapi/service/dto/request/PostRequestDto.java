package io.howstheairtoday.appcommunityexternalapi.service.dto.request;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PostRequestDto {

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class SaveRequestDto {

        /**
         * 게시글 내용
         */
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        /**
         * 게시글이 작성된 지역
         */
        @NotBlank(message = "지역을 입력해주세요.")
        private String region;

        /**
         * 게시글을 작성한 사용자의 아이디
         */
        @NotNull(message = "유저아이디가 존재하지 않습니다.")
        private UUID memberId;

        /**
         * 게시글에 첨부된 이미지 목록
         */
        // private List<PostImagesDto> postImageDtoList;

    }

    @Getter
    @Setter
    @Builder
    public static class PostImagesDto {

        /**
         * 게시글에 첨부된 이미지 URL
         */
        private MultipartFile postImageUrl;

        /**
         * 게시글에 첨부된 이미지 번호
         */
        private Integer postImageNumber;

    }

    public static class PostUUIDDto {

        @NotNull(message = "postUUID가 존재하지 않습니다.")
        @Getter
        private UUID postUUID;
    }

}
