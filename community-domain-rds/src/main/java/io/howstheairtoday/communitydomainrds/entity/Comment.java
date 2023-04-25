package io.howstheairtoday.communitydomainrds.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.howstheairtoday.communitydomainrds.common.BaseTimeEntity;
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
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post_comment")
@Getter
@ToString
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    //댓글 ID
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "comment_id")
    private UUID commentId;

    //댓글 내용
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "post")
    private Post post;

    //댓글 작성자 ID
    @Column(name = "memeber_id")
    private UUID memberId;

    //댓글 작성자 닉네임
    @Column(name = "memeber_nickname")
    private String nickName;

    //댓글 프로필 이미지
    @Column(name = "member_profile_image")
    private String memberProfileImage;

    @Builder
    public Comment(final String content, final Post post, final UUID memberId, final String nickName, final String memberProfileImage) {

        this.content = content;
        this.post = post;
        this.memberId = memberId;
        this.nickName = nickName;
        this.memberProfileImage = memberProfileImage;
    }

    //내용 수정 테스트 메소드
    public Comment updateContent(String content) {

        this.content = content;
        return this;
    }

}