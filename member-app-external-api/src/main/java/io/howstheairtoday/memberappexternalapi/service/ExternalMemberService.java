package io.howstheairtoday.memberappexternalapi.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.howstheairtoday.memberappexternalapi.service.dto.request.MemberRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExternalMemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public UUID createMember(MemberRequestDTO.SaveMemberRequestDto requestDTO) throws Exception {
        Member member = Member.builder()
            .loginId(requestDTO.getLoginId())
            .loginPassword(requestDTO.getLoginPassword())
            .email(requestDTO.getEmail())
            .nickname(requestDTO.getNickname())
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .build();
        member.encodePassword(passwordEncoder);
        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }
}
