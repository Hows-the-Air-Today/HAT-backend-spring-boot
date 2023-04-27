package io.howstheairtoday.airqualityappexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;

@ActiveProfiles("test")
@SpringBootTest
public class AirQualityRealTimeServiceTest {

    @DisplayName("가까운 측정소 위치 API 호출")
    @Test
    public void getNearTest() {

        // Given
        // 검증할 StationName의 기대값
        String expectedStationName = "연희";
        // RestTemplate를 통한 API 호출
        RestTemplate restTemplate = new RestTemplate();

        // url을 String으로 정의
        String queryParams = "/test/api";

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        // 예상되는 요청과 응답 설정
        String expectedResponse = "{\"response\":{\"body\":{\"totalCount\":3,\"items\":[{\"stationCode\":\"823651\",\"tm\":2,\"addr\":\"인천 서구 심곡로 98(심곡동) 인천광역시 인재개발원 옥상\",\"stationName\":\"연희\"},{\"stationCode\":\"823681\",\"tm\":2.7,\"addr\":\"인천 서구 거북로 116(석남동) 석남2동 행정복지센터 옥상\",\"stationName\":\"석남\"},{\"stationCode\":\"823705\",\"tm\":3,\"addr\":\"인천 서구 크리스탈로 131 수질정화시설관리동 2층 옥상\",\"stationName\":\"청라\"}],\"pageNo\":1,\"numOfRows\":10},\"header\":{\"resultMsg\":\"NORMAL_CODE\",\"resultCode\":\"00\"}}}";
        mockServer.expect(requestTo(queryParams))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));


        // When
        // restTemplate 대신에 mockServer를 사용하여 API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(queryParams, String.class);

        // Then

        // 목 서버 검증
        mockServer.verify();

        // JSON 객체에 있는 값을 사용하기 위한 작업
        assertNotNull(response.getBody());
        JSONObject root = new JSONObject(response.getBody());
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);

        // 측정소 명
        String stationName = item.getString("stationName");

        assertEquals(expectedStationName, stationName);
    }

    @Autowired
    private AirQualityRealTimeRepository airQualityRealTimeRepository;

    @DisplayName("사용자 요청 데이터 조회")
    @Test
    public void selectTest() {

        // Given
        String stationName = "종로구";

        // When
        AirQualityRealTime airQualityRealTime = airQualityRealTimeRepository.findAirQualityRealTimeByStationName(stationName);

        // Then
        Assertions.assertEquals(airQualityRealTime.getStationName(), stationName);
    }

    @DisplayName("Entity를 DTO로 변경")
    @Test
    public void entityToDTOTest(){

        // Given
        AirQualityRealTime airQualityRealTime = airQualityRealTimeRepository.findAirQualityRealTimeByStationName("종로구");

        // When
        CurrentDustResponseDTO currentDustResponseDTO = CurrentDustResponseDTO.builder()
            .airQualityRealTimeMeasurementId(airQualityRealTime.getAirQualityRealTimeMeasurementId())
            .sidoName(airQualityRealTime.getSidoName())
            .stationName(airQualityRealTime.getStationName())
            .so2Value(airQualityRealTime.getSo2Value())
            .coValue(airQualityRealTime.getCoValue())
            .o3Value(airQualityRealTime.getO3Value())
            .no2Value(airQualityRealTime.getNo2Value())
            .pm10Value(airQualityRealTime.getPm10Value())
            .pm25Value(airQualityRealTime.getPm25Value())
            .khaiValue(airQualityRealTime.getKhaiValue())
            .khaiGrade(airQualityRealTime.getKhaiGrade())
            .so2Grade(airQualityRealTime.getSo2Grade())
            .coGrade(airQualityRealTime.getCoGrade())
            .o3Grade(airQualityRealTime.getO3Grade())
            .no2Grade(airQualityRealTime.getNo2Grade())
            .pm10Grade(airQualityRealTime.getPm10Grade())
            .pm25Grade(airQualityRealTime.getPm25Grade())
            .dataTime(airQualityRealTime.getDataTime())
            .build();

        // Then

        Assertions.assertEquals(airQualityRealTime.getAirQualityRealTimeMeasurementId(), currentDustResponseDTO.getAirQualityRealTimeMeasurementId());
        Assertions.assertEquals(airQualityRealTime.getDataTime(), currentDustResponseDTO.getDataTime());
        Assertions.assertEquals(airQualityRealTime.getCoGrade(), currentDustResponseDTO.getCoGrade());
        Assertions.assertEquals(airQualityRealTime.getCoValue(), currentDustResponseDTO.getCoValue());
        Assertions.assertEquals(airQualityRealTime.getKhaiGrade(), currentDustResponseDTO.getKhaiGrade());
        Assertions.assertEquals(airQualityRealTime.getKhaiValue(), currentDustResponseDTO.getKhaiValue());
        Assertions.assertEquals(airQualityRealTime.getNo2Grade(), currentDustResponseDTO.getNo2Grade());
        Assertions.assertEquals(airQualityRealTime.getNo2Value(), currentDustResponseDTO.getNo2Value());
        Assertions.assertEquals(airQualityRealTime.getO3Grade(), currentDustResponseDTO.getO3Grade());
        Assertions.assertEquals(airQualityRealTime.getO3Value(), currentDustResponseDTO.getO3Value());
        Assertions.assertEquals(airQualityRealTime.getPm10Grade(), currentDustResponseDTO.getPm10Grade());
        Assertions.assertEquals(airQualityRealTime.getPm10Value(), currentDustResponseDTO.getPm10Value());
        Assertions.assertEquals(airQualityRealTime.getPm25Grade(), currentDustResponseDTO.getPm25Grade());
        Assertions.assertEquals(airQualityRealTime.getPm25Value(), currentDustResponseDTO.getPm25Value());
        Assertions.assertEquals(airQualityRealTime.getStationName(), currentDustResponseDTO.getStationName());
        Assertions.assertEquals(airQualityRealTime.getSidoName(), currentDustResponseDTO.getSidoName());
        Assertions.assertEquals(airQualityRealTime.getSo2Grade(), currentDustResponseDTO.getSo2Grade());
        Assertions.assertEquals(airQualityRealTime.getSo2Value(), currentDustResponseDTO.getSo2Value());
    }

    @DisplayName("베스트 10 조회 테스트")
    @Test
    public void selectBest10Test(){

        // Given
        Pageable pageable = PageRequest.of(0, 10);


        // When
        List<AirQualityRealTime> airQualityRealTimeList = airQualityRealTimeRepository.findBest10(pageable);

        // Then
        Assertions.assertEquals(10, airQualityRealTimeList.size());
    }

    @DisplayName("워스트 10 조회 테스트")
    @Test
    public void selectWorst10Test(){

        // Given
        Pageable pageable = PageRequest.of(0, 10);


        // When
        List<AirQualityRealTime> airQualityRealTimeList = airQualityRealTimeRepository.findWorst10(pageable);

        // Then
        Assertions.assertEquals(10, airQualityRealTimeList.size());
    }
}
