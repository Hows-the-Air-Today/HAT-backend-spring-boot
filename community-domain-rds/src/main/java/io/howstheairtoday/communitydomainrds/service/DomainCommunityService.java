package io.howstheairtoday.communitydomainrds.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableJpaAuditing
@Service
public class DomainCommunityService {

    private final PostRepository postRepository;

    /**
     *
     * @param post Entity객체 받은후 저장
     */
    @Transactional
    public void savePost(Post post) {

        postRepository.save(post);
    }


    /*
     *postRepository에서 id로 게시물을 찾아 Optional로 반환
     */
    public Optional<Post> findByPostId(Post post) {
        return postRepository.findById(post.getId());
    }

}
