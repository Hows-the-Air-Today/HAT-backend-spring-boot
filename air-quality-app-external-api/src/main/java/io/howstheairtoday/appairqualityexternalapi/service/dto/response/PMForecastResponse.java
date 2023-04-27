package io.howstheairtoday.appairqualityexternalapi.service.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PMForecastResponse {

    private final List<PMForecastData.ParticulateMatterForecast> pm10ForecastList;
    private final List<PMForecastData.ParticulateMatterForecast> pm25ForecastList;
}
