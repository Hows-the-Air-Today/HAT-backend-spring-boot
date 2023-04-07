package io.howstheairtoday.appcommunityexternalapi.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.CommentRequestDTO;
import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.repository.CommentRepository;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    //private final CommentRepository commentRepository;
    private final DomainCommunityService domainCommunityService;

    //게시물 댓글 작성 처리
    public void createComment(UUID postId, CommentRequestDTO commentRequestDTO){

        Comment comment = Comment.builder()
            .postId(postId)
            .content(commentRequestDTO.getContent())
            .memberId(commentRequestDTO.getMemberId())
            .build();

        domainCommunityService.saveComment(comment);
    }

}
