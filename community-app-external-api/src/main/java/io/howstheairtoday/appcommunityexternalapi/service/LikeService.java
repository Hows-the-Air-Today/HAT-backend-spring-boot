package io.howstheairtoday.appcommunityexternalapi.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.LikeRequestDTO;
import io.howstheairtoday.communitydomainrds.entity.Like;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final DomainCommunityService domainCommunityService;


    //좋아요 등록 및 삭제 로직
    public Like createLike(UUID postId, LikeRequestDTO likeRequestDTO){

        Optional<Like> like = domainCommunityService.changeStatus(postId, likeRequestDTO.getMemberId());

        Like liked;

        if(like.isPresent()){
            liked = like.get();
            liked.changeStatus();
            domainCommunityService.saveLike(liked);

            return liked;
        }

        liked = Like.builder()
            .liked(true)
            .memberId(likeRequestDTO.getMemberId())
            .postId(postId)
            .build();

        domainCommunityService.saveLike(liked);

        return liked;
    }

    //좋아요 개수 출력
    public int getLikeCount(UUID postId) {
        List<Like> likes = domainCommunityService.LikeCount(postId);
        int count = 0;

        for (Like like : likes) {
            if (like.getLiked()) {
                count++;
            }
        }
        return count;
    }
}
