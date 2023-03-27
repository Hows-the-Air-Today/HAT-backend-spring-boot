package io.howstheairtoday.service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AirRequestDTO {
    //시도별 이름
    private String sidoName;
    //측정소 명
    private String stationName;
}
