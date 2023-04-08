package io.howstheairtoday.memberappexternalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicationIdException extends RuntimeException {

    public DuplicationIdException(String message) {
        super(message);
    }
}
