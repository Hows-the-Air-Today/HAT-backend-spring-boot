package io.howstheairtoday.airqualityclient.exception;

public class RequestExecutionException extends RuntimeException {

    public RequestExecutionException(String message, Throwable cause) {

        super(message, cause);
    }
}
