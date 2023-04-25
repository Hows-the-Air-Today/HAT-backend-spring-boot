package io.howstheairtoday.communitydomainrds.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "post_like")
@Getter
@NoArgsConstructor
@ToString
public class Like {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "like_id")
    private UUID likeId;

    //좋아요 여부
    @Column(name = "liked")
    private Boolean liked;

    //좋아요 누른 사용자 ID
    @Column(name = "memeber_id")
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "post")
    private Post post;

    //좋아요 여부 변경
    public void changeStatus() {
        liked = !liked;
    }

    @Builder
    public Like(final Boolean liked, final Post post, final UUID memberId) {

        this.liked = liked;
        this.post = post;
        this.memberId = memberId;
    }

    //레포지토리 삭제 테스트 메소드
    public Like deleteLiked(Boolean liked) {

        this.liked = liked;
        return this;
    }

}
