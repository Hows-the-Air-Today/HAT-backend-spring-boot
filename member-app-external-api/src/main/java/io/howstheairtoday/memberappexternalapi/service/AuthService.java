package io.howstheairtoday.memberappexternalapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationIdException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationNicknameException;
import io.howstheairtoday.memberappexternalapi.exception.PasswordNotMatchedException;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // private final MemberExceptionHandler memberExceptionHandler;

    /**
     * 회원가입
     */
    public ApiResponse<?> register(SignUpRequestDTO requestDto) {
        String loginId = requestDto.getLoginId();
        String loginPassword = requestDto.getLoginPassword();
        String loginPasswordCheck = requestDto.getLoginPasswordCheck();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        // ID 중복 확인
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicationIdException("이미 사용 중인 아이디 입니다.");
        }

        // NickName 중복 확인
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicationNicknameException("이미 사용 중인 닉네임 입니다.");
        }

        // 비밀번호 일치 여부 확인
        if (!loginPassword.equals(loginPasswordCheck)) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다.");
        }

        // 회원 가입 처리
        Member registerMember = Member.builder()
            .loginId(loginId)
            .loginPassword(loginPassword)
            .email(email)
            .nickname(nickname)
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .build();
        registerMember.encodePassword(this.passwordEncoder);
        memberRepository.save(registerMember);

        return ApiResponse.res(HttpStatus.OK.value(), "회원 가입이 완료되었습니다.");
    }
}
