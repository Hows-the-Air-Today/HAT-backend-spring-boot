package io.howstheairtoday.airqualityappexternalapi.controller;

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
        String sidoName = "서울";
        String stationName = "종로구";
        String so2Value = "0.003";
        String coValue = "0.4";
        String o3Value = "0.027";
        String no2Value = "0.014";
        String pm10Value = "37";
        String pm25Value = "21";
        int khaiValue = 64;
        String khaiGrade = "2";
        String so2Grade = "1";
        String coGrade = "2";
        String o3Grade = "2";
        String no2Grade = "1";
        String pm10Grade = "1";
        String pm25Grade = "2";

        // When
        CurrentDustResponseDTO currentDustResponseDTO = externalApiService.selectAirQualityRealTime(stationName);

        // Then
        Assertions.assertEquals(sidoName, currentDustResponseDTO.getSidoName());
        Assertions.assertEquals(stationName, currentDustResponseDTO.getStationName());
        Assertions.assertEquals(so2Value, currentDustResponseDTO.getSo2Value());
        Assertions.assertEquals(coValue, currentDustResponseDTO.getCoValue());
        Assertions.assertEquals(o3Value, currentDustResponseDTO.getO3Value());
        Assertions.assertEquals(no2Value, currentDustResponseDTO.getNo2Value());
        Assertions.assertEquals(pm10Value, currentDustResponseDTO.getPm10Value());
        Assertions.assertEquals(pm25Value, currentDustResponseDTO.getPm25Value());
        Assertions.assertEquals(khaiValue, currentDustResponseDTO.getKhaiValue());
        Assertions.assertEquals(khaiGrade, currentDustResponseDTO.getKhaiGrade());
        Assertions.assertEquals(so2Grade, currentDustResponseDTO.getSo2Grade());
        Assertions.assertEquals(coGrade, currentDustResponseDTO.getCoGrade());
        Assertions.assertEquals(o3Grade, currentDustResponseDTO.getO3Grade());
        Assertions.assertEquals(no2Grade, currentDustResponseDTO.getNo2Grade());
        Assertions.assertEquals(pm10Grade, currentDustResponseDTO.getPm10Grade());
        Assertions.assertEquals(pm25Grade, currentDustResponseDTO.getPm25Grade());
    }
}
