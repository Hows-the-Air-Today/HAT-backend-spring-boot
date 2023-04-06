package io.howstheairtoday.service.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto extends User {

    private String loginId;
    private String loginPassword;

    public MemberDto(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginId = username;
        this.loginPassword = password;
    }

    // public MemberDto(String username, String password, boolean enabled, boolean accountNonExpired,
    //     boolean credentialsNonExpired, boolean accountNonLocked,
    //     Collection<? extends GrantedAuthority> authorities, String loginId,
    //     String loginPassword) {
    //     super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    //     this.loginId = loginId;
    //     this.loginPassword = loginPassword;
    // }
}
