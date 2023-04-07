package io.howstheairtoday.airqualitydomainrds.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurrentDustResponseDTO {

    // PK 및 랭킹
    private Long airQualityRealTimeMeasurementId;
    // 시도별 이름
    private String sidoName;
    // 측정소명
    private String stationName;
    // 아황산가스 농도
    private String so2Value;
    // 일산화탄소 농도
    private String coValue;
    // 오존 농도
    private String o3Value;
    // 이산화질소 농도
    private String no2Value;
    // 미세먼지(PM10) 농도
    private String pm10Value;
    // 미세먼지(PM2.5) 농도
    private String pm25Value;
    // 통합 대기 환경 수치
    private Integer khaiValue;
    // 통합 대기 환경 지수
    private String khaiGrade;
    // 아황산가스 지수
    private String so2Grade;
    // 일산화탄소 지수
    private String coGrade;
    // 오존 지수
    private String o3Grade;
    // 이산화질소 지수
    private String no2Grade;
    // 미세먼지(PM10) 24시간 등급자료
    private String pm10Grade;
    // 미세먼지(PM2.5) 24시간 등급자료
    private String pm25Grade;
    // 측정일시
    private LocalDateTime dataTime;
    // 작성일시
    private LocalDateTime createdAt;
    // 수정일시
    private LocalDateTime updatedAt;
}
