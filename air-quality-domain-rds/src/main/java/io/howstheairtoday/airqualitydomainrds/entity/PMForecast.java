package io.howstheairtoday.airqualitydomainrds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "particulate_matter_forecast")
@NoArgsConstructor
@Getter
public class PMForecast extends BaseEntity {

    // 미세먼지 주간예보 아이디
    @Id
    private Long particulateMatterForecastId;

    // 통보코드
    @Column(length = 4)
    @Size(max = 4)
    private String informCode;

    // 예보등급
    @Column(columnDefinition = "TEXT")
    private String informGrade;

    // 예보개황
    @Column(columnDefinition = "TEXT")
    private String informOverall;

    // 발생원인
    @Column(columnDefinition = "TEXT")
    private String informCause;

    // 행동요령
    @Column(columnDefinition = "TEXT")
    private String actionKnack;

    // 예측통보시간
    @Column(length = 10)
    @Size(max = 10)
    private String informData;

    // 통보시간
    @Column(length = 30)
    @Size(max = 30)
    private String dataTime;

    @Builder
    public PMForecast(Long particulateMatterForecastId, String informCode, String informGrade, String informOverall,
                      String informCause, String actionKnack, String informData, String dataTime) {

        this.particulateMatterForecastId = particulateMatterForecastId;
        this.informCode = informCode;
        this.informGrade = informGrade;
        this.informOverall = informOverall;
        this.informCause = informCause;
        this.actionKnack = actionKnack;
        this.informData = informData;
        this.dataTime = dataTime;
    }
}
