package io.howstheairtoday.communitydomainrds.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "post_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "member_image")
    private String memberImage;

    private String content;

    private String region;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Setter
    private List<PostImage> imageArray;

    @Builder
    public Post(final UUID id, final String content, final String region,
        final UUID memberId, final String memberNickname, final String memberImage) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.content = content;
        this.region = region;
        this.memberImage = memberImage;
        this.imageArray = new ArrayList<>();
    }

    public static Post createPost(final String content, final String region, final UUID memberId,
        final String memberNickname, final String memberImage) {
        return Post.builder()
            .content(content)
            .region(region)
            .memberId(memberId)
            .memberNickname(memberNickname)
            .memberImage(memberImage)
            .build();
    }

    public void deletePost() {
        this.setDeletedAt(LocalDateTime.now());
        if (imageArray != null) {
            imageArray.forEach(images ->
                images.setDeletedAt(LocalDateTime.now()));
        }
    }

    public void insertImages(PostImage postImage) {
        this.imageArray.add(postImage);
    }

    public void updatePost(String content, String region, List<PostImage> imageArray) {
        this.content = content;
        this.region = region;
        this.imageArray.clear();
        this.imageArray.addAll(imageArray);
        this.imageArray.forEach(image -> image.setPostId(this));
    }
}
