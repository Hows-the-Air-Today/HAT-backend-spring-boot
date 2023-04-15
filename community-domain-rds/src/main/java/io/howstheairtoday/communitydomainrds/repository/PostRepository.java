package io.howstheairtoday.communitydomainrds.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.howstheairtoday.communitydomainrds.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findById(UUID uuid);

}
