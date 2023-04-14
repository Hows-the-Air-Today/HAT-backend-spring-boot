package io.howstheairtoday.airqualityappbatch.util;

import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;

public class PMForecastDtoToEntityConverter {

    private PMForecastDtoToEntityConverter() {

        throw new AssertionError("Cannot instantiate PMForecastDtoToEntityConverter class.");
    }

    public static PMForecastResponse toEntity(PMForecast entity) {

        return PMForecastResponse.builder()
                .informCode(entity.getInformCode())
                .informGrade(entity.getInformGrade())
                .informOverall(entity.getInformOverall())
                .informCause(entity.getInformCause())
                .actionKnack(entity.getActionKnack())
                .informData(entity.getInformData())
                .dataTime(entity.getDataTime())
                .build();
    }
}
