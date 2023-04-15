package io.howstheairtoday.airqualityappbatch.util;

import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;

public class PMForecastDtoToEntityConverter {

    private PMForecastDtoToEntityConverter() {

        throw new AssertionError("Cannot instantiate PMForecastDtoToEntityConverter class.");
    }

    public static PMForecast toEntity(PMForecastResponse response) {

        return PMForecast.builder()
                .informCode(response.getInformCode())
                .informGrade(response.getInformGrade())
                .informOverall(response.getInformOverall())
                .informCause(response.getInformCause())
                .actionKnack(response.getActionKnack())
                .informData(response.getInformData())
                .dataTime(response.getDataTime())
                .build();
    }

    public static PMForecast toEntityWithId(PMForecastResponse response, Long id) {

        return PMForecast.builder()
                .particulateMatterForecastId(id)
                .informCode(response.getInformCode())
                .informGrade(response.getInformGrade())
                .informOverall(response.getInformOverall())
                .informCause(response.getInformCause())
                .actionKnack(response.getActionKnack())
                .informData(response.getInformData())
                .dataTime(response.getDataTime())
                .build();
    }
}
