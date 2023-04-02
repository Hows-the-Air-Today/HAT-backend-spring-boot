package io.howstheairtoday.memberappexternalapi.service.dto.request;

import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequestDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class SaveMemberRequestDto {

        /**
         * 회원가입시 필요한 내용
         */
        @NotBlank(message = "아이디를 입력해 주세요.")
        private String loginId;

        @NotEmpty(message = "비밀번호를 입력해 주세요.")
        private String loginPassword;

        @NotBlank(message = "인증 가능한 이메일을 입력해 주세요.")
        private String email;

        @NotBlank(message = "사용하실 닉네임을 입력해 주세요.")
        private String nickname;

        public Member toEntity() {
            return Member.builder()
                .loginId(loginId)
                .loginPassword(loginPassword)
                .email(email)
                .nickname(nickname)
                .memberProfileImage("default.jpg")
                .loginType(LoginType.LOCAL)
                .loginRole(LoginRole.ROLE_USER)
                .build();
        }
    }
}
