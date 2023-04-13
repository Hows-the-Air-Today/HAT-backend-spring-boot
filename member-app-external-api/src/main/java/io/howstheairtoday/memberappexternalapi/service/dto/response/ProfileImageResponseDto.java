package io.howstheairtoday.memberappexternalapi.service.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageResponseDto {

    private final UUID memberId;
    private final String memberProfileImage;

    @Builder
    public ProfileImageResponseDto(UUID memberId, String memberProfileImage) {
        this.memberId = memberId;
        this.memberProfileImage = memberProfileImage;
    }
}
