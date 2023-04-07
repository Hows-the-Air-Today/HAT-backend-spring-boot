package io.howstheairtoday.communitydomainrds.service;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.repository.CommentRepository;
import io.howstheairtoday.communitydomainrds.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 도메인 커뮤니티 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@EnableJpaAuditing
public class DomainCommunityService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    /**
     * 게시글 저장 메소드
     *
     * @param post 저장할 게시글 엔티티
     */
    @Transactional
    public void savePost(final Post post) {

        postRepository.save(post);
    }

    //게시글 댓글 저장 메소드
    @Transactional
    public void saveComment(Comment comment){

        commentRepository.save(comment);
    }

}
