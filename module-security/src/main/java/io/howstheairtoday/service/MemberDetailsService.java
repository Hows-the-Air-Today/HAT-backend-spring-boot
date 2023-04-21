package io.howstheairtoday.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
import io.howstheairtoday.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Spring Security를 위한 MemberDetailsService
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> result = memberRepository.findByLoginId(username);
        Member member = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find LoginId"));

        log.info("🛠️ MemberDetailsService member -------------------- 🛠️");

        // deletedAt 컬럼이 null인 경우에만 로그인 허용
        if (member.getDeletedAt() != null) {
            throw new UsernameNotFoundException("탈퇴된 회원 정보 입니다.");
        }

        MemberDto dto = new MemberDto(
            member.getLoginId(),
            member.getLoginPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER")));

        log.info("🛠️ MemberLoginRequestDTO =====> " + dto);

        return dto;
    }
}
