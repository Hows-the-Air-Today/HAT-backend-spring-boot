package io.howstheairtoday.memberappexternalapi.service.dto.request;

import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @NotBlank(message = "아이디를 입력해 주세요.")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력해 주세요.")
    private String loginPassword;

    @NotEmpty(message = "동일한 비밀번호를 입력해 주세요")
    private String loginPasswordCheck;

    @NotBlank(message = "인증 가능한 이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "사용하실 닉네임을 입력해 주세요.")
    private String nickname;
}
