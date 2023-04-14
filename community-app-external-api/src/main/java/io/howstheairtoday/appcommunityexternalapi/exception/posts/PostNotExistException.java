package io.howstheairtoday.appcommunityexternalapi.exception.posts;

import io.howstheairtoday.appcommunityexternalapi.exception.CommonBusinessException;
import io.howstheairtoday.appcommunityexternalapi.exception.dto.ErrorMessage;

public class PostNotExistException extends CommonBusinessException {
    public PostNotExistException() {
        super(ErrorMessage.NOT_EXIST_POST);
    }
}
