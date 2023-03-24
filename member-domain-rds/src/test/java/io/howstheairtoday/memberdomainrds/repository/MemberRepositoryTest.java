package io.howstheairtoday.memberdomainrds.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
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
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    // @AfterEach
    // public void clean() {
    //     memberRepository.deleteAll();
    // }

    @Test
    @DisplayName("회원 정보 저장")
    @Transactional
    @Rollback(false)
    void saveMemberInfo() {

        // given
        memberRepository.save(Member.builder()
            .loginId("hat_id")
            .loginPassword("test123")
            .email("kcshat@gmail.com")
            .nickname("hat")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .token("TEST.REFRESH.TOKEN")
            .build());

        // when
        List<Member> memberList = memberRepository.findAll();

        // then
        Member member = memberList.get(0);
        assertThat(member.getLoginId()).isEqualTo("hat_id");
    }

    @Test
    @DisplayName("회원 정보 삭제")
    public void deleteMemberInfo() {

        // given
        Member member = Member.builder()
            .loginId("hat_id")
            .loginPassword("test123")
            .email("kcshat@gmail.com")
            .nickname("hat")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .token("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(member);

        // when
        memberRepository.delete(member);
        // then
        Optional<Member> deletedMember = memberRepository.findByMemberId(member.getMemberId());
        assertThat(deletedMember.isPresent()).isFalse();
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void modifyMemberInfo() {

        // given
        Member savedmember = Member.builder()
            .loginId("hat_id")
            .loginPassword("test123")
            .email("kcshat@gmail.com")
            .nickname("hat")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .token("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(savedmember);

        // when
        String modifiedPassword = "modtest123";
        String modifiedNickname = "modHat";
        String modifiedProfileImage = "mod.jpg";

        Member modifiedMember = savedmember.modifiedMember(modifiedPassword, modifiedNickname, modifiedProfileImage);
        memberRepository.save(modifiedMember);

        // then
        Optional<Member> findMember = memberRepository.findByMemberId(savedmember.getMemberId());
        assertThat(findMember.isPresent()).isTrue();

        Member member = findMember.get();
        assertThat(member.getLoginPassword()).isEqualTo(modifiedPassword);
        assertThat(member.getNickname()).isEqualTo(modifiedNickname);
        assertThat(member.getMemberProfileImage()).isEqualTo(modifiedProfileImage);
    }

    @Test
    @DisplayName("회원 정보 조회")
    public void getMemberInfo() {

        // given
        Member savedmember = Member.builder()
            .loginId("hat_id")
            .loginPassword("test123")
            .email("kcshat@gmail.com")
            .nickname("hat")
            .memberProfileImage("default.jpg")
            .loginType(LoginType.LOCAL)
            .loginRole(LoginRole.ROLE_USER)
            .token("TEST.REFRESH.TOKEN")
            .build();
        memberRepository.save(savedmember);

        // when
        Optional<Member> foundMember = memberRepository.findByMemberId(savedmember.getMemberId());

        // then
        assertThat(foundMember.get().getLoginId()).isEqualTo("hat_id");
    }
}