package io.howstheairtoday.appairqualityexternalapi.util;

import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;
import io.howstheairtoday.appairqualityexternalapi.service.dto.response.PMForecastData;
import io.howstheairtoday.appairqualityexternalapi.service.dto.response.PMForecastResponse;

import java.util.Arrays;
import java.util.List;

public class PMForecastEntityToDtoConverter {

    private PMForecastEntityToDtoConverter() {

        throw new AssertionError("Cannot instantiate PMForecastEntityToDtoConverter class.");
    }

    public static PMForecastResponse toDto(List<PMForecast> pm10EntityList, List<PMForecast> pm25EntityList) {

        List<PMForecastData.ParticulateMatterForecast> pm10ForecastData = pm10EntityList.stream()
                .map(PMForecastEntityToDtoConverter::toParticulateMatterForecast)
                .toList();

        List<PMForecastData.ParticulateMatterForecast> pm25ForecastData = pm25EntityList.stream()
                .map(PMForecastEntityToDtoConverter::toParticulateMatterForecast)
                .toList();

        return new PMForecastResponse(pm10ForecastData, pm25ForecastData);
    }

    private static PMForecastData.ParticulateMatterForecast toParticulateMatterForecast(PMForecast entity) {

        List<PMForecastData.RegionalGrade> informGradeList = parseInformGradeToRegionalGradeList(entity.getInformGrade());

        return PMForecastData.ParticulateMatterForecast.builder()
                .informCode(entity.getInformCode())
                .informOverall(entity.getInformOverall())
                .informCause(entity.getInformCause())
                .actionKnack(entity.getActionKnack())
                .informGradeList(informGradeList)
                .informData(entity.getInformData())
                .dataTime(entity.getDataTime())
                .build();
    }

    private static List<PMForecastData.RegionalGrade> parseInformGradeToRegionalGradeList(String informGrade) {

        return Arrays.stream(informGrade.split(","))
                .map(PMForecastEntityToDtoConverter::createRegionalGrade)
                .toList();
    }

    private static PMForecastData.RegionalGrade createRegionalGrade(String regionGradeInfo) {

        String[] regionAndGrade = regionGradeInfo.trim().split(":");

        if (regionAndGrade.length != 2) {
            throw new IllegalArgumentException("Invalid regionGradeInfo: " + regionGradeInfo);
        }

        return PMForecastData.RegionalGrade.builder()
                .region(regionAndGrade[0].trim())
                .grade(regionAndGrade[1].trim())
                .build();
    }
}
