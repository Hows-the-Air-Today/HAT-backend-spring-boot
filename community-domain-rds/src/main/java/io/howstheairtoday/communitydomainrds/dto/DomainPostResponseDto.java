package io.howstheairtoday.communitydomainrds.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DomainPostResponseDto {

    @AllArgsConstructor
    @Getter
    @Builder
    public static class PostImageDto {
        private String postImageUrl;
        private int postImageNumber;
        private UUID memberId;
        private UUID postId;

    }
}
