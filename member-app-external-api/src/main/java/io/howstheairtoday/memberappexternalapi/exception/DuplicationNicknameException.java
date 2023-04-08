package io.howstheairtoday.memberappexternalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicationNicknameException extends RuntimeException {

    public DuplicationNicknameException(String message) {
        super(message);
    }
}
