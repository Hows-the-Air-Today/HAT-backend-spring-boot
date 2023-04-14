package io.howstheairtoday.memberappexternalapi.service.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangePasswordResponseDto {

    private final UUID memberId;
    private final String loginPassword;

    @Builder
    public ChangePasswordResponseDto(UUID memberId, String loginPassword) {
        this.memberId = memberId;
        this.loginPassword = loginPassword;
    }
}
