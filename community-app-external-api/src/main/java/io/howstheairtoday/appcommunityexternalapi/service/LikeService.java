package io.howstheairtoday.appcommunityexternalapi.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.LikeRequestDTO;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.LikeResponseDTO;
import io.howstheairtoday.communitydomainrds.entity.Like;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final DomainCommunityService domainCommunityService;


    //좋아요 등록 및 삭제 로직
    public LikeResponseDTO createLike(UUID postId, LikeRequestDTO likeRequestDTO){

        Optional<Like> like = domainCommunityService.changeStatus(postId, likeRequestDTO.getMemberId());

        Like liked;
        LikeResponseDTO likedto;

        if(like.isPresent()){
            liked = like.get();
            liked.changeStatus();
            domainCommunityService.saveLike(liked);

            likedto = LikeResponseDTO.builder()
                .isLike(liked.getLiked())
                .likeCount(getLikeCount(postId))
                .build();

            return likedto;
        }

        liked = Like.builder()
            .liked(true)
            .memberId(likeRequestDTO.getMemberId())
            .postId(postId)
            .build();

        domainCommunityService.saveLike(liked);

        likedto = LikeResponseDTO.builder()
            .isLike(liked.getLiked())
            .likeCount(getLikeCount(postId))
            .build();

        return likedto;
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
