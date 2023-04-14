package io.howstheairtoday.memberappexternalapi.service.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyNicknameResponseDto {

    private final UUID memberId;
    private final String nickname;

    @Builder
    public ModifyNicknameResponseDto(UUID memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
