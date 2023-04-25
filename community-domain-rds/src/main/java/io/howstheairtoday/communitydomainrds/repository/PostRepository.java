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

    @Query(
        "SELECT p FROM Post p JOIN FETCH p.imageArray WHERE p.memberId = :memberId AND p.deletedAt IS NULL ORDER BY p"
            + ".createdAt DESC")
    List<Post> findByMemberIdAndDeletedAtIsNull(@Param("memberId") UUID memberId);

}
