package io.howstheairtoday.appairqualityexternalapi.service.dto.response;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class AirQualityRankingResponse {

    private final List<CurrentDustResponseDTO> bestRankingList;
    private final List<CurrentDustResponseDTO> worstRankingList;
}
