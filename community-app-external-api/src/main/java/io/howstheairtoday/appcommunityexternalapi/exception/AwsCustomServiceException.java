package io.howstheairtoday.appcommunityexternalapi.exception;

import io.howstheairtoday.appcommunityexternalapi.exception.dto.ErrorMessage;

public class AwsCustomServiceException extends CommonBusinessException {
    public AwsCustomServiceException() {
        super(ErrorMessage.UPLOAD_FAIL_INTERNAL_SERVER_ERROR);
    }
}
