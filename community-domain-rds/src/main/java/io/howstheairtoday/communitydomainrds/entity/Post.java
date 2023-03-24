package io.howstheairtoday.communitydomainrds.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "post_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "member_id")
    private UUID userId;

    private String content;

    private String location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Getter
    private List<PostImage> imageArray;

    @Builder
    public Post(UUID id, String content, String location, UUID userId) {
        this.userId = userId;
        this.content = content;
        this.location = location;
        this.imageArray = new ArrayList<>();
    }

    public static Post createPost(String content, String location) {
        return Post.builder()
            .location(location)
            .content(content)
            .build();
    }

    public void updateFeed(Post post, List<PostImage> imageArray) {
        this.content = post.getContent();
        this.location = post.getLocation();
        this.imageArray = imageArray;
    }

    public void deletePost() {
        this.setDeletedAt(LocalDateTime.now());
        imageArray.forEach(images -> images.setDeletedAt(LocalDateTime.now()));
    }

    public void imagesAdd(PostImage postImage) {
        this.imageArray.add(postImage);
    }

}
