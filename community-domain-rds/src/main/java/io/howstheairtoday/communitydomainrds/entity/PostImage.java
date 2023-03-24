package io.howstheairtoday.communitydomainrds.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "post_image_id",columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "post_image_number")
    private Integer postImageNumber;

    @Column(name = "post_image")
    private String postImageUrl;

    @Builder
    public PostImage(Integer postImageNumber, String postImageUrl, Post post) {
        this.postImageNumber = postImageNumber;
        this.postImageUrl = postImageUrl;
        this.post = post;
    }

    public static PostImage createImages(Integer postImageNumber, String postImageUrl, Post post) {
        return PostImage.builder()
            .postImageNumber(postImageNumber).postImageUrl(postImageUrl).post(post).build();
    }

    public void updatePostImage(String postImageUrl, Integer postImageNumber) {
        this.postImageUrl = postImageUrl;
        this.postImageNumber = postImageNumber;
    }

}