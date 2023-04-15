package io.howstheairtoday.appcommunityexternalapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.howstheairtoday.appcommunityexternalapi.exception.posts.PostNotExistException;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
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
            saveRequestDto.getRegion(), saveRequestDto.getMemberId());

        postImages.forEach(
            postImg -> post.insertImages(
                PostImage.create(postImg.getPostImageNumber(), postImg.getPostImageUrl(),
                    post, post.getMemberId())));

        domainCommunityService.savePost(post);
    }

    public void updatePost(final PostRequestDto.SaveRequestDto saveRequestDto, final UUID uuid) {

        final List<PostImage> postImageList = new ArrayList<>();

        final Post post = domainCommunityService.findById(uuid).orElseThrow(PostNotExistException::new);

        saveRequestDto.getPostImageDtoList().forEach(postImageDto -> {
            PostImage postImages = PostImage.builder()
                .postImageUrl(postImageDto.getPostImageUrl())
                .postImageNumber(postImageDto.getPostImageNumber())
                .build();
            postImageList.add(postImages);
        });
        post.updatePost(saveRequestDto.getContent(), saveRequestDto.getRegion(), postImageList);

        domainCommunityService.savePost(post);

    }

    public PostResponseDto.PostResponseDetail getDetailPost(UUID postsId) {

        Post getDetailPost = domainCommunityService.findById(postsId).orElseThrow(PostNotExistException::new);

        List<PostResponseDto.PostImageResponseDto> imagesDtos = getDetailPost.getImageArray().stream()
            .map(postImage -> PostResponseDto.PostImageResponseDto.builder()
                .imageId(postImage.getPostImageId())
                .memberId(postImage.getMemberId())
                .postId(postImage.getPostId().getId())
                .imageNumber(postImage.getPostImageNumber())
                .imageUrl(postImage.getPostImageUrl())
                .createdAt(postImage.getCreatedAt())
                .updatedAt(postImage.getUpdatedAt())
                .deletedAt(postImage.getDeletedAt())
                .build())
            .collect(Collectors.toList());

        PostResponseDto.PostDto postDto = PostResponseDto.PostDto.builder()
            .postId(getDetailPost.getId())
            .region(getDetailPost.getRegion())
            .memberId(getDetailPost.getMemberId())
            .content(getDetailPost.getContent())
            .deletedAt(getDetailPost.getDeletedAt())
            .updatedAt(getDetailPost.getUpdatedAt())
            .createdAt(getDetailPost.getCreatedAt())
            .build();

        PostResponseDto.PostResponseDetail getPostresponseDetail = PostResponseDto.PostResponseDetail.builder()
            .postDto(postDto)
            .imageDto(imagesDtos)
            .build();

        return getPostresponseDetail;

    }
}
