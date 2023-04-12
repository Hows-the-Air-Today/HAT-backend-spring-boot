package io.howstheairtoday.memberappexternalapi.service;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.common.MemberExceptionHandler;
import io.howstheairtoday.memberappexternalapi.exception.ConflictException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationIdException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationNicknameException;
import io.howstheairtoday.memberappexternalapi.exception.NotFoundException;
import io.howstheairtoday.memberappexternalapi.exception.PasswordNotMatchedException;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ChangePasswordRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ModifyNicknameRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ModifyProfileImageRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberappexternalapi.service.dto.response.MemberIdResponseDto;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ModifyNicknameResponseDto;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ProfileImageResponseDto;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ProfileResponseDto;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberExceptionHandler memberExceptionHandler;

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

    /**
     * 회원 정보 조회 - 마이페이지
     */
    public ProfileResponseDto read(UUID memberId) {
        Optional<Member> result = memberRepository.findByMemberId(memberId);
        Member member = result.orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다."));
        return modelMapper.map(member, ProfileResponseDto.class);
    }

    /**
     * 회원 정보 수정 - 마이페이지
     */
    @Transactional
    public ApiResponse<?> modifyNickname(ModifyNicknameRequestDto request) {

        Member member = memberRepository.findByMemberId(request.getMemberId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new ConflictException("이미 사용 중인 닉네임입니다.");
        }

        member.modifiyNickname(request.getNickname());
        ModifyNicknameResponseDto response = ModifyNicknameResponseDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .build();
        return ApiResponse.res(HttpStatus.OK.value(), "닉네임 변경이 완료 되었습니다.", response);
    }
}
