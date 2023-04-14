package io.howstheairtoday.airqualityappbatch.util;

import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;

public class PMForecastEntityToDtoConverter {

    private PMForecastEntityToDtoConverter() {

        throw new AssertionError("Cannot instantiate PMForecastEntityToDtoConverter class.");
    }

    public static PMForecast toDto(PMForecastResponse response) {

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
}
