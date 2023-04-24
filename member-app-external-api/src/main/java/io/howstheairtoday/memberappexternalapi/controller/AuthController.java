package io.howstheairtoday.memberappexternalapi.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.service.AuthService;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ChangePasswordRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.ModifyNicknameRequestDto;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ProfileResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    public ApiResponse<Object> register(@Valid @RequestBody final SignUpRequestDTO signUpRequestDto) {
        authService.register(signUpRequestDto);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.CREATED.value())
            .msg("회원가입이 완료 되었습니다.")
            .build();
    }

    /**
     * 회원 정보 조회 - 마이페이지
     */
    @GetMapping("/{memberId}")
    public ApiResponse<ProfileResponseDto> read(@PathVariable("memberId") UUID memberId) {
        ProfileResponseDto responseDto = authService.read(memberId);
        return ApiResponse.<ProfileResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("success")
            .data(responseDto)
            .build();
    }

    /**
     * 회원 닉네임 수정
     */
    @PatchMapping("/nickname")
    public ApiResponse<Object> modifyNickname(@Valid @RequestBody final ModifyNicknameRequestDto request) {
        authService.modifyNickname(request);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("닉네임 수정이 완료 되었습니다.")
            .build();
    }

    /**
     * 회원 비밀번호 변경
     */
    @PatchMapping("/password")
    public ApiResponse<Object> changePassword(@Valid @RequestBody final ChangePasswordRequestDto request) {
        authService.changePassword(request);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("비밀번호 변경이 완료 되었습니다.")
            .build();
    }

    /**
     * 프로필 이미지 변경
     */
    @PatchMapping("/profile-change")
    public ApiResponse<Object> modifyProfileImg(@RequestPart("profileImg") MultipartFile memberProfileImage,
        UUID memberId) throws IOException {
        log.info("memberProfileImage" + memberProfileImage);
        log.info("memberId" + memberId);

        String path = authService.modifyProfileImg(memberId, memberProfileImage);

        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("이미지 변경이 완료 되었습니다.")
            .data(path)
            .build();
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout() {
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("로그아웃이 완료되었습니다.")
            .build();
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/{memberId}")
    public ApiResponse<Object> delete(@PathVariable("memberId") UUID memberId) {
        authService.delMember(memberId);
        return ApiResponse.<Object>builder()
            .statusCode(HttpStatus.OK.value())
            .msg("회원탈퇴가 완료되었습니다.")
            .build();
    }
}