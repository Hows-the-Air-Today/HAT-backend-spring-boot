package io.howstheairtoday.appcommunityexternalapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExternalPostService {

    private final DomainCommunityService domainCommunityService;

    public void createPost(PostRequestDto.SaveRequestDto saveRequestDto) {

        List<PostRequestDto.PostImagesDto> postImages = saveRequestDto.getPostImageDtoList();

        Post post = Post.createPost(saveRequestDto.getContent(), saveRequestDto.getLocation());

        postImages.forEach(
            postImg -> post.imagesAdd(
                PostImage.createImages(postImg.getPostImageNumber(), postImg.getPostImageUrl(), post)));

        domainCommunityService.savePost(post);
    }

}
