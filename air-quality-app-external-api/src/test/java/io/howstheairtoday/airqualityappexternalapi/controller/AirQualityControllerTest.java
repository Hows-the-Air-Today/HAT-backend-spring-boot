package io.howstheairtoday.airqualityappexternalapi.controller;

import io.howstheairtoday.airqualityappexternalapi.common.AbstractRestDocsTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("대기 정보 API 테스트")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AirQualityControllerTest extends AbstractRestDocsTests {

    @DisplayName("대기질 실시간 조회")
    @Test
    public void selectAirQualityRealTime() throws Exception {

        // Given
        double tmX = 200817.7459347529;
        double tmY = 443071.11499739345;

        // When
        // MockMvc 요청과 응답 설정
        ResultActions resultActions = mockMvc.perform(get("/api/v1/airquality/tm?tmx=" + tmX + "&tmy=" + tmY));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("대기질 주간 예보 조회")
    @Test
    public void getAllPMForecastData() throws Exception {

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/airquality/forecast"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("미세먼지/초미세먼지 주간 예보 정보 조회 성공"))
                .andExpect(jsonPath("$.data.pm10ForecastList").isNotEmpty())
                .andExpect(jsonPath("$.data.pm25ForecastList").isNotEmpty())
                .andDo(print());
    }

    @DisplayName("대기질 실시간 랭킹 정보 조회")
    @Test
    public void getAirQualityRanking() throws Exception {

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/airquality/ranking"));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}
