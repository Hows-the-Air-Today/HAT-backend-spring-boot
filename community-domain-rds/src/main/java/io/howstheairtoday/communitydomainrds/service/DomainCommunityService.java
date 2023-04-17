package io.howstheairtoday.communitydomainrds.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import io.howstheairtoday.communitydomainrds.dto.CommentPageDTO;
import io.howstheairtoday.communitydomainrds.dto.CommentPageListDTO;
import io.howstheairtoday.communitydomainrds.dto.DomainPostResponseDto;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
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

    public Optional<Post> findById(final UUID uuid) {
        return postRepository.findById(uuid);
    }

    //게시글 댓글 저장 메소드
    @Transactional
    public Comment saveComment(Comment comment) {

        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public List<DomainPostResponseDto.PostImageDto> getMyPost(UUID memberId) {
        List<PostImage> list = postRepository.findByMemberIdAndDeletedAtIsNull(memberId);

        List<DomainPostResponseDto.PostImageDto> domainPostImageDto = list.stream().
            map(postImage -> new DomainPostResponseDto.PostImageDto(postImage.getPostImageUrl(),
                postImage.getPostImageNumber(), postImage.getMemberId(), postImage.getPostImageId()))
            .collect(Collectors.toList());
        return domainPostImageDto;
    }

    //게시물 ID 검색 메소드
    @Transactional
    public Optional<Comment> findCommentId(UUID commentID) {

        return commentRepository.findByCommentId(commentID);
    }

    //게시물 페이징 메소드
    @Transactional
    public CommentPageListDTO getComment(UUID postId, Integer page) {

        int size = 10;
        Sort.Direction direction = Sort.Direction.ASC;
        String sort = "createdAt";

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Slice<Comment> comments = commentRepository.findByPostIdAndDeletedAtIsNull(postId, pageable);

        List<CommentPageDTO> commentDTOs = comments.getContent().stream()
            .map(comment -> {
                CommentPageDTO commentPageDTO = CommentPageDTO.builder()
                    .commentId(comment.getCommentId())
                    .content(comment.getContent())
                    .memberId(comment.getMemberId())
                    .postId(comment.getPostId())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();

                return commentPageDTO;

            }).collect(Collectors.toList());

        return new CommentPageListDTO(commentDTOs, comments.hasNext(), comments.getNumber(), comments.getSize(),
            comments.isFirst(), comments.isLast());
    }
}
