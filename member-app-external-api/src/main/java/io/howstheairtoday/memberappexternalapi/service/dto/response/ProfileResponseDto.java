package io.howstheairtoday.memberappexternalapi.service.dto.response;

import java.util.UUID;

import io.howstheairtoday.memberdomainrds.entity.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private UUID memberId;
    private String loginId;
    private String email;
    private String nickname;
    private String memberProfileImage;
    private LoginType loginType;
}

