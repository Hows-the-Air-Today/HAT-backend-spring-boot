package io.howstheairtoday.communitydomainrds.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 엔티티 클래스
 */
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    /**
     * 게시글 ID
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "post_id", columnDefinition = "BINARY(16)")
    private UUID id;

    /**
     * 작성자 ID
     */
    @Column(name = "member_id")
    private UUID userId;

    /**
     * 게시글 내용
     */
    private String content;

    /**
     * 게시글 위치
     */
    private String location;

    /**
     * 게시글 이미지 목록
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> imageArray;

    /**
     * Post Builder
     */
    @Builder
    public Post(final UUID id, final String content, final String location,
        final UUID userId) {

        this.userId = userId;
        this.content = content;
        this.location = location;
        this.imageArray = new ArrayList<>();
    }

    /**
     * 게시글 생성 메소드
     *
     * @param content 게시글 내용
     * @param location 게시글 위치
     * @return 생성된 게시글
     */
    public static Post createPost(final String content, final String location) {
        return Post.builder()
            .content(content)
            .location(location)
            .build();
    }

    /**
     * 게시글 삭제 메소드
     */
    public void deletePost() {
        this.setDeletedAt(LocalDateTime.now());
        if (imageArray != null) {
            imageArray.forEach(images ->
                images.setDeletedAt(LocalDateTime.now()));
        }
    }

    /**
     * 게시글 이미지 추가 메소드
     *
     * @param postImage 추가할 게시글 이미지
     */

    public void imagesAdd(PostImage postImage) {
        this.imageArray.add(postImage);
    }

}
