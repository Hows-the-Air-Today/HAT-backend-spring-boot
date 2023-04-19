package io.howstheairtoday.communitydomainrds.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findById(UUID uuid);

    @Query("SELECT pi FROM PostImage pi WHERE pi.memberId = :memberId AND pi.deletedAt IS NULL")
    List<PostImage> findByMemberIdAndDeletedAtIsNull(@Param("memberId") UUID memberId);


}
