package io.howstheairtoday.memberappexternalapi.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.common.MemberExceptionHandler;
import io.howstheairtoday.memberappexternalapi.exception.ConflictException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationIdException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationNicknameException;
import io.howstheairtoday.memberappexternalapi.exception.NotFoundException;
import io.howstheairtoday.memberappexternalapi.exception.PasswordNotMatchedException;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ChangePasswordRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ModifyNicknameRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ChangePasswordResponseDto;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ModifyNicknameResponseDto;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ProfileResponseDto;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import io.howstheairtoday.modulecore.service.AwsS3UploadService;
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
    private final AwsS3UploadService awsS3UploadService;

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
            // S3에 등록된 기본 이미지
            .memberProfileImage("https://kakao4-hat-bucket.s3.ap-northeast-2.amazonaws.com/member/hat-default.png")
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
        Optional<Member> result = memberRepository.findByMemberIdAndDeletedAtIsNull(memberId);
        Member member = result.orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 존재하지 않습니다."));
        return modelMapper.map(member, ProfileResponseDto.class);
    }

    /**
     * 회원 닉네임 수정
     */
    @Transactional
    public ApiResponse<?> modifyNickname(ModifyNicknameRequestDto request) {

        Member member = memberRepository.findByMemberIdAndDeletedAtIsNull(request.getMemberId())
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

    /**
     * 회원 패스워드 변경
     */
    @Transactional
    public ApiResponse<?> changePassword(ChangePasswordRequestDto request) {

        String loginPassword = request.getLoginPassword();
        String loginPasswordCheck = request.getLoginPasswordCheck();

        Member member = memberRepository.findByMemberIdAndDeletedAtIsNull(request.getMemberId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        if (!loginPassword.equals(loginPasswordCheck)) {
            throw new PasswordNotMatchedException("비밀번호가 서로 일치하지 않습니다.");
        }

        // 새로운 비밀번호 설정
        String encodedPassword = passwordEncoder.encode(request.getLoginPassword());
        member.changePassword(encodedPassword);
        ChangePasswordResponseDto result = ChangePasswordResponseDto.builder()
            .memberId(member.getMemberId())
            .loginPassword(member.getLoginPassword())
            .build();
        return ApiResponse.res(HttpStatus.OK.value(), "비밀번호 변경이 완료되었습니다.", result);
    }

    /**
     * 회원 프로필 이미지 수정
     */
    @Transactional
    public String modifyProfileImg(UUID memberId, MultipartFile profileImage) throws IOException {

        Member findMember = memberRepository.findByMemberIdAndDeletedAtIsNull(memberId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        String path = awsS3UploadService.uploadImages(profileImage, "프로필이미지");

        findMember.modifyProfileImage(path);
        return path;
    }

    /**
     * 회원 탈퇴 기능 - Soft Delete
     */
    @Transactional
    public ApiResponse<?> delMember(UUID memberId) {

        Member member = memberRepository.findByMemberIdAndDeletedAtIsNull(memberId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        member.setDeletedAt(LocalDateTime.now());
        memberRepository.save(member);
        return ApiResponse.res(HttpStatus.OK.value(), "탈퇴가 완료되었습니다.");
    }
}
