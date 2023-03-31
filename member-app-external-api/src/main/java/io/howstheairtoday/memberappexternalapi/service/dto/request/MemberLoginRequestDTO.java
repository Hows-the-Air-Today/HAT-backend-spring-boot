package io.howstheairtoday.memberappexternalapi.service.dto.request;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Spring Security을 위한 DTO 생성 - 사용자 로그인
 */
@Getter
@Setter
@ToString
public class MemberLoginRequestDTO extends User {

    private String loginId;
    private String loginPassword;

    public MemberLoginRequestDTO(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginId = username;
        this.loginPassword = password;
    }
}
