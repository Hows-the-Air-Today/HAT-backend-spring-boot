package io.howstheairtoday.service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AirRequestDTO {
    private String sidoName;
    private String stationName;
}
