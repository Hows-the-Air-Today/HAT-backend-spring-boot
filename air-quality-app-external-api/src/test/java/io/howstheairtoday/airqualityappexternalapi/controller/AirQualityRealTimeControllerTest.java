package io.howstheairtoday.airqualityappexternalapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AirQualityRealTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("조회 서비스를 사용하는 컨트롤러 테스트")
    @Test
    public void selectAirQualityRealTimeTest() throws Exception{

        // Given
        String umdName = "서초동";

        // When
        // MockMvc 요청과 응답 설정
        ResultActions resultActions = mockMvc.perform(get("/api/v1/airquality/stationName/{umdName}", umdName));

        // Then
        resultActions.andExpect(status().isOk()).andDo(print());
    }
}
