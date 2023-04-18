package io.howstheairtoday.appcommunityexternalapi.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDTO {

    private boolean isLike;

    private int likeCount;
}
