package io.howstheairtoday.appcommunityexternalapi.service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.howstheairtoday.communitydomainrds.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDTO {

    private UUID commentId;

    private String content;

    private UUID memberId;

    private String nickName;

    private String memberProfileImage;

    private Post post;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

}