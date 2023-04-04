package io.howstheairtoday.airqualityappexternalapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.appairqualityexternalapi.service.ExternalApiService;

@ActiveProfiles("test")
@SpringBootTest
public class ControllerTests {
    @Autowired
    private ExternalApiService externalApiService;

    @DisplayName("조회 서비스를 사용하는 컨트롤러 테스트")
    @Test
    public void selectAirQualityRealTimeTest(){

        // Given
        String stationName = "종로구";

        Map<String, CurrentDustResponseDTO> data = new HashMap<>();

        // When
        data.put("airQuality", externalApiService.selectAirQualityRealTime(stationName));
        CurrentDustResponseDTO currentDustResponseDTO = data.get("airQuality");

        // Then
        Assertions.assertNotNull(currentDustResponseDTO);

    }
}
