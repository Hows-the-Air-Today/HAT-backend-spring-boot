package io.howstheairtoday.communitydomainrds.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.howstheairtoday.communitydomainrds.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
