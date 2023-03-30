package io.howstheairtoday.memberappexternalapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.service.AuthService;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
import io.howstheairtoday.memberappexternalapi.service.dto.response.ProfileResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
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
}