package io.howstheairtoday.appcommunityexternalapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

/**
 * api-external post service
 */
@Service
@RequiredArgsConstructor
public class ExternalPostService {

    private final DomainCommunityService domainCommunityService;

    /**
     * 게시글 생성을 처리합니다.
     *
     * @param saveRequestDto 생성할 게시글 정보
     */
    public void createPost(final PostRequestDto.SaveRequestDto saveRequestDto) {

        final List<PostRequestDto.PostImagesDto> postImages =
            saveRequestDto.getPostImageDtoList();

        final Post post = Post.createPost(saveRequestDto.getContent(),
            saveRequestDto.getRegion());

        postImages.forEach(
            postImg -> post.insertImages(
                PostImage.create(postImg.getPostImageNumber(), postImg.getPostImageUrl(),
                    post)));

        domainCommunityService.savePost(post);
    }

}
