package io.howstheairtoday.communitydomainrds.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private UUID id;

    @Column(name = "member_id")
    private UUID userId;

    private String content;

    private String region;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    private List<PostImage> imageArray;

    @Builder
    public Post(UUID id, String content, String region, UUID userId, List<PostImage> imageArray) {
        this.userId = userId;
        this.content = content;
        this.region = region;
        this.imageArray = imageArray;
    }

    public void updateFeed(Post post, List<PostImage> imageArray) {
        this.content = post.getContent();
        this.region = post.getRegion();
        this.imageArray = imageArray;
    }

    public void deletePost() {
        this.setDeletedAt(LocalDateTime.now());
        imageArray.forEach(images -> images.setDeletedAt(LocalDateTime.now()));
    }

}
