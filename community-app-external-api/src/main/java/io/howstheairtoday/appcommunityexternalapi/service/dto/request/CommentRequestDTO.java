package io.howstheairtoday.appcommunityexternalapi.service.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Builder

public class CommentRequestDTO {

    //게시물 댓글 내용
    private String content;
    //댓글 작성자 아이디
    private UUID memberId;
}

