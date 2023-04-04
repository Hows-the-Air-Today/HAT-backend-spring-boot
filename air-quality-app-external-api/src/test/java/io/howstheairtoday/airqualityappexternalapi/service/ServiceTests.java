package io.howstheairtoday.airqualityappexternalapi.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;

@ActiveProfiles("test")
@SpringBootTest
public class ServiceTests {

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
        List<AirQualityRealTime> currentDustResponseDTO = airQualityRealTimeRepository.findAirQualityRealTimeByStationName(stationName);

        // Then
        Assertions.assertEquals(currentDustResponseDTO.get(0).getStationName(), stationName);
    }
}
