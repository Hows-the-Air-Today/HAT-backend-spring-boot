package io.howstheairtoday.airqualityappexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;

@ActiveProfiles("test")
@SpringBootTest
public class AirQualityRealTimeServiceTest {

    // TM 좌표 및 근처 측정소 정보를 찾기 위한 API 키
    @Value("${air.informationkey}")
    private String informationkey;

    // 공공데이터 포털에서 TM좌표를 받아오기 위한 url
    @Value("${air.tmUrl}")
    private String tmUrl;

    // 공공데이터 포털에서 근처 측정소 위치를 받아오기 위한 url
    @Value("${air.nearUrl}")
    private String nearUrl;

    @DisplayName("TM 좌표를 계산해주는 API 호출")
    @Test
    public void getTMTest() {

        // Given
        String umdName = "서구 가정동"; // 검증할 읍면동 이름
        String expectedTMX = "171207.04807"; // 기대하는 tmX 좌표
        String expectedTMY = "447286.409085"; // 기대하는 tmY 좌표
        RestTemplate restTemplate = new RestTemplate();

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // When
        // url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + informationkey
            + "&returnType=json"
            + "&numOfRows=100"
            + "&pageNo=1"
            + "&umdName=" + umdName;

        ResponseEntity<String> response = restTemplate.exchange(tmUrl + queryParams, HttpMethod.GET, entity,
            String.class);

        // Then

        // JSON 객체에 있는 값을 사용하기 위한 작업
        assertNotNull(response.getBody());
        JSONObject root = new JSONObject(response.getBody());
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);

        // tmX, tmY 좌표
        String actualTMX = item.getString("tmX");
        String actualTMY = item.getString("tmY");
        assertEquals(expectedTMX, actualTMX);
        assertEquals(expectedTMY, actualTMY);
    }

    @DisplayName("가까운 측정소 위치 API 호출")
    @Test
    public void getNearTest() {

        // Given
        // 검증할 StationName의 기대값
        String expectedStationName = "연희";
        // 검증할 읍면동의 TM 좌표 정의
        String tmX = "171207.04807"; // tmX 좌표
        String tmY = "447286.409085"; // tmY 좌표
        RestTemplate restTemplate = new RestTemplate();

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // When
        // url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + informationkey
            + "&returnType=json"
            + "&tmX=" + tmX
            + "&tmY=" + tmY
            + "&ver=1.1";

        // RestTemplate를 통한 API 호출
        ResponseEntity<String> response = restTemplate.exchange(nearUrl + queryParams, HttpMethod.GET, entity,
            String.class);

        // Then

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
}
