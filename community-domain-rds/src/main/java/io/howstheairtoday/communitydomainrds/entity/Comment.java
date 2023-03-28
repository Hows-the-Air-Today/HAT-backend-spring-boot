package io.howstheairtoday.communitydomainrds.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "post_comment")
@Getter
@NoArgsConstructor
@ToString
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "comment_id")
    private UUID commentId; //댓글 ID

    @Column(name = "content")
    private String content; //댓글 내용

    @Column(name = "post_id")
    private UUID postId; //게시물 ID

    @Column(name = "memeber_id")
    private UUID memberId; //댓글 작성자 ID

    @Builder
    public Comment(final UUID commentId, final String content, final UUID postId, final UUID memberId) {
        this.content = content;
        this.postId = postId;
        this.memberId = memberId;
    }

    public Comment changeContent(String content) {
        this.content = content;
        return this;
    }

}