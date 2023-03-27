package io.howstheairtoday.airqualitydomainrds.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AirQualityRealTime extends BaseEntity {
    @Id
    //PK 및 랭킹
    private Long air_quality_real_time_measurement_id;
    //시도별 이름
    private String sido_name;
    //측정소명
    private String station_name;
    //아황산가스 농도
    private String so2_value;
    //일산화탄소 농도
    private String co_value;
    //오존 농도
    private String o3_value;
    //이산화질소 농도
    private String no2_value;
    //미세먼지(PM10) 농도
    private String pm10_value;
    //미세먼지(PM2.5) 농도
    private String pm25_value;
    //통합 대기 환경 수치
    private String khai_value;
    //통합 대기 환경 지수
    private String khai_grade;
    //아황산가스 지수
    private String so2_grade;
    //일산화탄소 지수
    private String co_grade;
    //오존 지수
    private String o3_grade;
    //이산화질소 지수
    private String no2_grade;
    //미세먼지(PM10) 24시간 등급자료
    private String pm10_grade;
    //미세먼지(PM2.5) 24시간 등급자료
    private String pm25_grade;
    //측정일시
    private LocalDateTime data_time;
}
