package io.howstheairtoday.appairqualityexternalapi.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PMForecastData {

    @Builder
    @Getter
    public static class ParticulateMatterForecast {

        // 통보코드 (PM10 or PM25)
        private String informCode;

        // 예보개황
        private String informOverall;

        // 발생원인
        private String informCause;

        // 행동요령
        private String actionKnack;

        // 지역별 예보등급
        private List<RegionalGrade> informGradeList;

        // 예측통보시간
        private String informData;

        // 통보시간 (발표시간)
        private String dataTime;
    }

    @Builder
    @Getter
    public static class RegionalGrade {

        // 지역명
        private String region;

        // 예보등급
        private String grade;
    }
}
