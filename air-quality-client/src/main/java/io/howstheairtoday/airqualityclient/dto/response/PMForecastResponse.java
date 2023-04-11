package io.howstheairtoday.airqualityclient.dto.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class PMForecastResponse {

    // 통보 코드
    private String informCode;

    // 예보 등급
    private String informGrade;

    // 예보 개황
    private String informOverall;

    // 발생 원인
    private String informCause;

    // 행동 요령
    private String actionKnack;

    // 예측 통보 시간
    private String informData;

    // 통보 시간
    private String dataTime;
}
