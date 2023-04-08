package io.howstheairtoday.memberdomainrds.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.howstheairtoday.memberdomainrds.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMemberId(UUID memberId);

    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String Nickname);
}