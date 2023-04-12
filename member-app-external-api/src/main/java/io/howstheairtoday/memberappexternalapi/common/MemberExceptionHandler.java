package io.howstheairtoday.memberappexternalapi.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.howstheairtoday.memberappexternalapi.exception.ConflictException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationIdException;
import io.howstheairtoday.memberappexternalapi.exception.DuplicationNicknameException;
import io.howstheairtoday.memberappexternalapi.exception.NotFoundException;
import io.howstheairtoday.memberappexternalapi.exception.PasswordNotMatchedException;

@ControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicationIdException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicationIdException(DuplicationIdException e) {
        ApiResponse<?> apiResponse = ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(DuplicationNicknameException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicationNicknameException(DuplicationNicknameException e) {
        ApiResponse<?> apiResponse = ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<ApiResponse<?>> handlePasswordNotMatchedException(PasswordNotMatchedException e) {
        ApiResponse<?> apiResponse = ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<?>> ConflictException(ConflictException e) {
        ApiResponse<?> apiResponse = ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> NotFoundException(NotFoundException e) {
        ApiResponse<?> apiResponse = ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
