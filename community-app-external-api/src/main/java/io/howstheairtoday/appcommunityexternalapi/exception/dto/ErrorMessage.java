package io.howstheairtoday.appcommunityexternalapi.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    CONFLICT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버와 연결이 끊어졌습니다."),
    NOT_EXIST_POST(HttpStatus.NOT_FOUND, "게시물이 존재하지 않습니다."),
    NOT_MEMBER_ID(HttpStatus.NOT_FOUND, "멤버 아이디가 없습니다."),
    UPLOAD_FAIL_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "업로드를 실패하였습니다.");

    private final HttpStatus status;
    private final String description;
}
