package io.howstheairtoday.communitydomainrds.service;

import java.util.List;
import java.util.Optional;

import org.aspectj.apache.bcel.Repository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@EnableJpaAuditing
@Service
public class DomainCommunityService {

    private final PostRepository postRepository;

    @Transactional
    public void savePost(Post post) {

        postRepository.save(post);
    }

    public Optional<Post> findByPostId(Post post) {
        return postRepository.findById(post.getId());
    }

}
