package io.howstheairtoday.appmemberexternalapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.howstheairtoday.appmemberexternalapi.service.dto.MemberLoginRequestDTO;
import io.howstheairtoday.memberdomainrds.entity.Member;
import io.howstheairtoday.memberdomainrds.repository.MemberRepository;
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

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> result = memberRepository.findByLoginId(username);
        Member member = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find LoginId"));

        log.info("🛠️ MemberDetailsService member -------------------- 🛠️");

        MemberLoginRequestDTO dto = new MemberLoginRequestDTO(
            member.getLoginId(),
            member.getLoginPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER")));

        log.info("🛠️ MemberLoginRequestDTO =====> " + dto);

        return dto;
    }
}
