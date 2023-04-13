package io.howstheairtoday.memberdomainrds.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MemberRepository.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.memberdomainrds.repository")
@EntityScan("io.howstheairtoday.memberdomainrds.entity")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 정보 저장")
    @Test
    void saveMemberInfo() {

        // given
        Member member = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember).isEqualTo(member);
    }

    @DisplayName("회원 닉네임 수정")
    @Test
    public void modifiedMemberNickname() {

        // given
        Member member = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        Member savedMember = memberRepository.save(member);
        savedMember.modifiyNickname("modNick");

        // when
        List<Member> memberList = memberRepository.findAll();
        Member result = memberList.get(0);

        // then
        Assertions.assertEquals(result.getNickname(), "modNick");
    }

    @DisplayName("회원 비밀번호 변경")
    @Test
    public void changeMemberPassword() {

        // given
        Member member = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        Member savedMember = memberRepository.save(member);
        savedMember.changePassword("mod123");

        // when
        Optional<Member> foundMember = memberRepository.findByMemberId(savedMember.getMemberId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getLoginPassword()).isEqualTo("mod123");
    }

    @DisplayName("회원 프로필 이미지 수정")
    @Test
    public void modifiedMemberProfileImage() {

        // given
        Member member = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        Member savedMember = memberRepository.save(member);
        savedMember.modifyProfileImage("modify.jpg");

        // when
        Optional<Member> foundMember = memberRepository.findByMemberId(savedMember.getMemberId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getMemberProfileImage()).isEqualTo("modify.jpg");
    }

    @DisplayName("회원 정보 삭제")
    @Test
    public void deleteMemberInfo() {

        // given
        Member member = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(member);

        // when
        memberRepository.delete(member);

        // then
        Optional<Member> deletedMember = memberRepository.findByMemberId(member.getMemberId());
        assertThat(deletedMember).isEmpty();
    }

    @DisplayName("회원 정보 조회")
    @Test
    public void getMemberInfo() {

        Member savedmember = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(savedmember);

        // when
        Optional<Member> foundMember = memberRepository.findByMemberId(savedmember.getMemberId());

        // then
        assertThat(foundMember.get().getLoginId()).isEqualTo("testId");
    }
}