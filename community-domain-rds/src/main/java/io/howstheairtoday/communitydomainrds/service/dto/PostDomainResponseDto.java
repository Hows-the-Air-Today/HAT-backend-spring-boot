package io.howstheairtoday.communitydomainrds.service.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDomainResponseDto {

    private List<Map<String, Object>> data;

    @Builder
    public PostDomainResponseDto(List<Map<String, Object>> data) {
        this.data = data;
    }
}


