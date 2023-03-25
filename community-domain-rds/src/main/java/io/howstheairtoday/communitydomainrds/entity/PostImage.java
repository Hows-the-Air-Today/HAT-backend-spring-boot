package io.howstheairtoday.communitydomainrds.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostImage extends BaseTimeEntity {

    /**
     *  UUID 타입 id 필드
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "post_image_id", columnDefinition = "BINARY(16)")
    private UUID id;

    /**
     *  Post 엔티티와 연관된 PostImage 엔티티
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 포스트의 이미지 번호
     */
    @Column(name = "post_image_number")
    private Integer postImageNumber;

    /**
     * 포스트 이미지 URL
     */
    @Column(name = "post_image")
    private String postImageUrl;

    /**
     * PostImage 인스턴스를 생성하는 빌더 메서드
     * @param postImageNumber    이미지 순서 번호
     * @param postImageUrl      이미지 URL
     * @param post              해당 이미지와 연관된 포스트 엔티티
     */
    @Builder
    public PostImage(Integer postImageNumber, String postImageUrl, Post post) {
        this.postImageNumber = postImageNumber;
        this.postImageUrl = postImageUrl;
        this.post = post;
    }

    /**
     * 이미지 정보를 받아서 PostImage 인스턴스를 생성하는 메서드
     * @param postImageNumber    이미지 순서 번호
     * @param postImageUrl      이미지 URL
     * @param post              해당 이미지와 연관된 포스트 엔티티
     * @return 생성된 PostImage 인스턴스
     */
    public static PostImage createImages(Integer postImageNumber, String postImageUrl, Post post) {
        return PostImage.builder()
            .postImageNumber(postImageNumber).postImageUrl(postImageUrl).post(post).build();
    }

    /**
     * 이미지 정보를 업데이트하는 메서드
     * @param postImageUrl      업데이트할 이미지 URL
     * @param postImageNumber   업데이트할 이미지 순서 번호
     */
    public void updatePostImage(String postImageUrl, Integer postImageNumber) {
        this.postImageUrl = postImageUrl;
        this.postImageNumber = postImageNumber;
    }

}

