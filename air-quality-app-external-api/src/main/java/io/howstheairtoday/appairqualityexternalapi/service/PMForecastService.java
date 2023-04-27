package io.howstheairtoday.appairqualityexternalapi.service;

import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;
import io.howstheairtoday.airqualitydomainrds.repository.PMForecastRepository;
import io.howstheairtoday.appairqualityexternalapi.service.dto.response.PMForecastResponse;
import io.howstheairtoday.appairqualityexternalapi.util.PMForecastEntityToDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PMForecastService {

    private final PMForecastRepository pmForecastRepository;

    public PMForecastResponse getAllPMForecastData() {

        List<PMForecast> pm10Entity = pmForecastRepository.findByInformCode("PM10");
        List<PMForecast> pm25Entity = pmForecastRepository.findByInformCode("PM25");

        return PMForecastEntityToDtoConverter.toDto(pm10Entity, pm25Entity);
    }
}
