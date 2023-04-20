package io.howstheairtoday.appcommunityexternalapi.service.dto.response;

import io.howstheairtoday.communitydomainrds.service.dto.PostDomainResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostExternalDto {

    PostDomainResponseDto postList;

}