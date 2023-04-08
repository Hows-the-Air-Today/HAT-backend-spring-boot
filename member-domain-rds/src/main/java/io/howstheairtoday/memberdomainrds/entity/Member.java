package io.howstheairtoday.memberdomainrds.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID memberId;

    @Column(nullable = false, length = 40)
    private String loginId; // 아이디

    @Column(nullable = false, length = 128)
    private String loginPassword;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(length = 128)
    private String memberProfileImage;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginRole loginRole;

    @Column(length = 255, nullable = true)
    private String refreshToken;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.loginPassword = passwordEncoder.encode(loginPassword);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @Builder
    public Member(String loginId, String loginPassword, String email, String nickname, String memberProfileImage,
        LoginType loginType,
        LoginRole loginRole, String refreshToken) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.email = email;
        this.nickname = nickname;
        this.memberProfileImage = memberProfileImage;
        this.loginType = loginType;
        this.loginRole = loginRole;
        this.refreshToken = refreshToken;
    }

    public Member modifiedMember(
        final String modifiedPassword,
        final String modifiedNickname,
        final String modifiedProfileImage) {
        this.loginPassword = modifiedPassword;
        this.nickname = modifiedNickname;
        this.memberProfileImage = modifiedProfileImage;
        return this;
    }
}
