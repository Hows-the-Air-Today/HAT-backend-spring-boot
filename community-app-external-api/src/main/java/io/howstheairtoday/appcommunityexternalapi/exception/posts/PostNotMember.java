package io.howstheairtoday.appcommunityexternalapi.exception.posts;

import io.howstheairtoday.appcommunityexternalapi.exception.CommonBusinessException;
import io.howstheairtoday.appcommunityexternalapi.exception.dto.ErrorMessage;

public class PostNotMember extends CommonBusinessException {

    public PostNotMember() {
        super(ErrorMessage.NOT_MEMBER_ID);
    }
}
