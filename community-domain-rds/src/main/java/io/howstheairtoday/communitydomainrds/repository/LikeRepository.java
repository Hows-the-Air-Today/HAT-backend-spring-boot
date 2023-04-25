package io.howstheairtoday.communitydomainrds.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.howstheairtoday.communitydomainrds.entity.Like;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    //게시물 좋아요 총 개수 출력
    List<Like> findLikeByPostIdIsAndLikedIsTrue(UUID postId);

    //좋아요 여부 판단
    Optional<Like> findLikeByPostIdAndMemberId(UUID postId, UUID memberId);
}

