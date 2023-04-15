package io.howstheairtoday.communitydomainrds.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.howstheairtoday.communitydomainrds.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findByCommentId(UUID commentId);

    Page<Comment> findByPostIdAndDeletedAtIsNull(UUID postId, Pageable pageable);
}