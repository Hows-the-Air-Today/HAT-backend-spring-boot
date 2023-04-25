package io.howstheairtoday.appcommunityexternalapi.service;

import java.time.LocalDateTime;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import io.howstheairtoday.appcommunityexternalapi.exception.AwsCustomServiceException;
import io.howstheairtoday.appcommunityexternalapi.exception.posts.PostNotExistException;
import io.howstheairtoday.appcommunityexternalapi.exception.posts.PostNotMember;
import io.howstheairtoday.appcommunityexternalapi.service.dto.request.PostRequestDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostExternalDto;
import io.howstheairtoday.appcommunityexternalapi.service.dto.response.PostResponseDto;
import io.howstheairtoday.communitydomainrds.dto.DomainPostResponseDto;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.PostImage;
import io.howstheairtoday.communitydomainrds.service.DomainCommunityService;
import io.howstheairtoday.communitydomainrds.service.dto.PostDomainResponseDto;
import io.howstheairtoday.modulecore.service.AwsS3UploadService;
import lombok.RequiredArgsConstructor;

/**
 * api-external post service
 */
@Service
@RequiredArgsConstructor
public class ExternalPostService {

    private final DomainCommunityService domainCommunityService;

    private final AwsS3UploadService awsS3UploadService;

    /**
     * 게시글 생성을 처리합니다.
     *
     * @param saveRequestDto 생성할 게시글 정보
     */
    public void createPost(final PostRequestDto.SaveRequestDto saveRequestDto,
        List<MultipartFile> postImages) {

        final Post post = Post.createPost(saveRequestDto.getContent(),
            saveRequestDto.getRegion(), saveRequestDto.getMemberId(), saveRequestDto.getMemberNickname(),
            saveRequestDto.getMemberImageUrl());

        postImages.forEach(
            postImg -> {
                String uploadImage = "";
                try {
                    uploadImage = awsS3UploadService.uploadImages(postImg, "게시판");
                } catch (IOException e) {
                    throw new AwsCustomServiceException();
                }
                post.insertImages(
                    PostImage.create(uploadImage,
                        post, post.getMemberId()));

            });

        domainCommunityService.savePost(post);
    }

    public void updatePost(final PostRequestDto.SaveRequestDto saveRequestDto, final UUID uuid,
        List<MultipartFile> postImagesDtos) {

        final List<PostImage> postImageList = new ArrayList<>();

        final Post post = domainCommunityService.findById(uuid).orElseThrow(PostNotExistException::new);

        postImagesDtos.forEach(postImageDto -> {
            String updateImage = null;
            try {
                updateImage = awsS3UploadService.uploadImages(postImageDto, "게시판");
            } catch (IOException e) {
                throw new AwsCustomServiceException();
            }
            PostImage postImages = PostImage.builder()
                .postImageUrl(updateImage)
                .build();
            postImageList.add(postImages);
        });
        post.updatePost(saveRequestDto.getContent(), saveRequestDto.getRegion(), postImageList);

        domainCommunityService.savePost(post);

    }

    public void deletePost(final UUID postsId) {

        Post post = domainCommunityService.findById(postsId)
            .orElseThrow(PostNotExistException::new);

        post.deletePost();

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
            .memberNickname(getDetailPost.getMemberNickname())
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

    public PostExternalDto getPostQsl(String region, LocalDateTime createdAt, int limit) {

        PostDomainResponseDto data = domainCommunityService.getPostWithImage(region, createdAt, limit);

        return new PostExternalDto(data);
    }

    public List<PostResponseDto.PostImageDto> getMyPost(UUID memberId) {
        List<DomainPostResponseDto.PostImageDto> post = domainCommunityService.getMyPost(memberId);
        List<PostResponseDto.PostImageDto> list =
            post.stream()
                .map(postImage -> new PostResponseDto.PostImageDto(postImage.getPostImageUrl(),
                    postImage.getMemberId(), postImage.getPostId()))
                .collect(Collectors.toList());
        return list;
    }
}
