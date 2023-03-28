package io.howstheairtoday.memberdomainrds.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.howstheairtoday.memberdomainrds.entity.LoginRole;
import io.howstheairtoday.memberdomainrds.entity.LoginType;
import io.howstheairtoday.memberdomainrds.entity.Member;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MemberRepository.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.memberdomainrds.repository")
@EntityScan("io.howstheairtoday.memberdomainrds.entity")
    // @Transactional(propagation = Propagation.NOT_SUPPORTED)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    // @AfterEach
    // public void clean() {
    //     memberRepository.deleteAll();
    // }

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

    @DisplayName("회원 정보 수정")
    @Test
    public void modifyMemberInfo() {

        // given
        Member oldMember = Member.builder()
            .loginId("testId")
            .loginPassword("test123")
            .email("test@test.com")
            .nickname("testNick")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .refreshToken("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(oldMember);

        // when
        String modifiedPassword = "modtest123";
        String modifiedNickname = "modtestnick";
        String modifiedProfileImage = "modimage.jpg";

        Member modifiedMember = oldMember.modifiedMember(modifiedPassword, modifiedNickname, modifiedProfileImage);
        memberRepository.save(modifiedMember);

        // then
        Optional<Member> findMember = memberRepository.findByMemberId(oldMember.getMemberId());
        assertThat(findMember.isPresent()).isTrue();

        Member member = findMember.get();
        assertThat(member.getLoginPassword()).isEqualTo(modifiedPassword);
        assertThat(member.getNickname()).isEqualTo(modifiedNickname);
        assertThat(member.getMemberProfileImage()).isEqualTo(modifiedProfileImage);
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