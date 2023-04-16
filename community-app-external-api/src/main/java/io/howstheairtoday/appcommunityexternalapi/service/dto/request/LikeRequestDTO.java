package io.howstheairtoday.appcommunityexternalapi.service.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeRequestDTO {

    private UUID memberId;
}
