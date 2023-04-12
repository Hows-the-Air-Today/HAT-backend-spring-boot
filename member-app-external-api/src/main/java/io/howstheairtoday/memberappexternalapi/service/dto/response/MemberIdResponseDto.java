package io.howstheairtoday.memberappexternalapi.service.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResponseDto {

    private final UUID memberId;

    @Builder
    public MemberIdResponseDto(UUID memberId) {
        this.memberId = memberId;
    }
}
