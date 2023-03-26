package io.howstheairtoday.airqualitydomainrds.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityRealTime extends BaseEntity {
    @Id
    private Long air_quality_real_time_measurement_id;
    private String sido_name;
    private String station_name;
    private String so2_value;
    private String co_value;
    private String o3_value;
    private String no2_value;
    private String pm10_value;
    private String pm25_value;
    private String khai_value;
    private String khai_grade;
    private String so2_grade;
    private String co_grade;
    private String o3_grade;
    private String no2_grade;
    private String pm10_grade;
    private String pm25_grade;
    private LocalDateTime data_time;
}
