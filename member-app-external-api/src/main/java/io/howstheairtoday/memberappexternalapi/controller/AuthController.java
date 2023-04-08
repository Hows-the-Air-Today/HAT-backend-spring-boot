package io.howstheairtoday.memberappexternalapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.service.AuthService;
import io.howstheairtoday.memberappexternalapi.service.dto.request.SignUpRequestDTO;
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
}
