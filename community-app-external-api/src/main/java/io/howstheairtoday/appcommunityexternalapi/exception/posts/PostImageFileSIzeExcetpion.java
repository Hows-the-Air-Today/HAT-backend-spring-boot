package io.howstheairtoday.appcommunityexternalapi.exception.posts;

import io.howstheairtoday.appcommunityexternalapi.exception.CommonBusinessException;
import io.howstheairtoday.appcommunityexternalapi.exception.dto.ErrorMessage;

public class PostImageFileSIzeExcetpion extends CommonBusinessException {

    public PostImageFileSIzeExcetpion(String s) {
        super(ErrorMessage.UPLOAD_FAIL_SIZE, s);
    }

    public PostImageFileSIzeExcetpion() {
        super(ErrorMessage.UPLOAD_FAIL_SIZE);
    }

}
