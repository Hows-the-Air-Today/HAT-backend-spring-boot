package io.howstheairtoday.memberappexternalapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.memberappexternalapi.common.ApiResponse;
import io.howstheairtoday.memberappexternalapi.service.ExternalMemberService;
import io.howstheairtoday.memberappexternalapi.service.dto.request.MemberRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final ExternalMemberService externalMemberService;

    @PostMapping("/account/signup")
    public ResponseEntity<ApiResponse<UUID>> createMember(@Valid @RequestBody final MemberRequestDTO.SaveMemberRequestDto requestDTO) {

        try {
            UUID memberId = externalMemberService.createMember(requestDTO);
            return ResponseEntity.ok(ApiResponse.res(HttpStatus.OK.value(), "회원가입에 성공했습니다.", memberId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.res(HttpStatus.BAD_REQUEST.value(), "회원가입에 실패했습니다."));
        }
    }
}
