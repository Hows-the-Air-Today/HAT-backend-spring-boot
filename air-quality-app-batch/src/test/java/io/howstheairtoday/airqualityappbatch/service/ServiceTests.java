package io.howstheairtoday.airqualityappbatch.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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
import io.howstheairtoday.service.AirQualityRealTimeService;
import io.howstheairtoday.service.dto.AirResponseDTO;

@ActiveProfiles("test")
@SpringBootTest
public class ServiceTests {

    @Value("${air.apikey}") // application.yml에 저장된 서비스키 값
    private String airapiKey;
    @Autowired
    private AirQualityRealTimeService airQualityRealTimeService;
    @Autowired
    private AirQualityRealTimeRepository airQualityRealTimeRepository;

    @DisplayName("시도별 대기 정보를 찾는 API 호출")
    @Test
    public void apiTest() {
        // Given
        //공공데이터 포털에서 시도별 대기 정보를 받아오기 위한 url
        String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

        //RestTemplate를 통한 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // When
        //url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + airapiKey
            + "&returnType=json"
            + "&numOfRows=642"
            + "&pageNo=1"
            + "&sidoName=전국"
            + "&ver=1.0";

        //restTemplate를 통한 API 호출
        ResponseEntity<String> response = restTemplate.exchange(url + queryParams, HttpMethod.GET, entity,
            String.class);

        // Then

        //JSON 객체에 있는 값을 사용하기 위한 작업
        assertNotNull(response.getBody());
        JSONObject root = new JSONObject(response.getBody());
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);

        //측정소명
        String sidoName = item.optString("sidoName");
        String stationName = item.optString("stationName");
        String so2Value = item.optString("so2Value");
        String coValue = item.optString("coValue");
        String o3Value = item.optString("o3Value");
        String no2Value = item.optString("no2Value");
        String pm10Value = item.optString("pm10Value");
        String pm25Value = item.optString("pm25Value");
        String khaiValue = item.optString("khaiValue");
        String khaiGrade = item.optString("khaiGrade");
        String so2Grade = item.optString("so2Grade");
        String coGrade = item.optString("coGrade");
        String o3Grade = item.optString("o3Grade");
        String no2Grade = item.optString("no2Grade");
        String pm10Grade = item.optString("pm10Grade");
        String pm25Grade = item.optString("pm25Grade");

        assertNotNull(sidoName);
        assertNotNull(stationName);
        assertNotNull(so2Value);
        assertNotNull(coValue);
        assertNotNull(o3Value);
        assertNotNull(no2Value);
        assertNotNull(pm10Value);
        assertNotNull(pm25Value);
        assertNotNull(khaiValue);
        assertNotNull(khaiGrade);
        assertNotNull(so2Grade);
        assertNotNull(coGrade);
        assertNotNull(o3Grade);
        assertNotNull(no2Grade);
        assertNotNull(pm10Grade);
        assertNotNull(pm25Grade);
    }

    @DisplayName("시도별 대기 정보를 데이터베이스에 저장")
    @Test
    public void insertTest() {
        // Given
        List<AirResponseDTO> airResponseDTOList = airQualityRealTimeService.getAirQualityData();

        // When

        // 데이터베이스에 값을 저장하기 위한 작성
        AirQualityRealTime airQualityRealTime;
        for (int i = 0; i < airResponseDTOList.size(); i++) {
            airQualityRealTime = AirQualityRealTime.builder()
                .air_quality_real_time_measurement_id((long)i)
                .sido_name(airResponseDTOList.get(i).getSido_name())
                .station_name(airResponseDTOList.get(i).getStation_name())
                .so2_value(airResponseDTOList.get(i).getSo2_value())
                .co_value(airResponseDTOList.get(i).getCo_value())
                .o3_value(airResponseDTOList.get(i).getO3_value())
                .no2_value(airResponseDTOList.get(i).getNo2_value())
                .pm10_value(airResponseDTOList.get(i).getPm10_value())
                .pm25_value(airResponseDTOList.get(i).getPm25_value())
                .khai_value(airResponseDTOList.get(i).getKhai_value())
                .khai_grade(airResponseDTOList.get(i).getKhai_grade())
                .so2_grade(airResponseDTOList.get(i).getSo2_grade())
                .co_grade(airResponseDTOList.get(i).getCo_grade())
                .o3_grade(airResponseDTOList.get(i).getO3_grade())
                .no2_grade(airResponseDTOList.get(i).getNo2_grade())
                .pm10_grade(airResponseDTOList.get(i).getPm10_grade())
                .pm25_grade(airResponseDTOList.get(i).getPm25_grade())
                .data_time(airResponseDTOList.get(i).getData_time())
                .build();
            airQualityRealTimeRepository.save(airQualityRealTime);
        }

        // Then

        // 데이터 베이스 값을 조회해서 확인
        List<AirQualityRealTime> airQualityRealTimeList = airQualityRealTimeRepository.findAll();

        assertNotNull(airQualityRealTimeList);

        for (int i = 0; i < airResponseDTOList.size(); i++) {
            assertEquals((long)i, airQualityRealTimeList.get(i).getAir_quality_real_time_measurement_id());
            assertEquals(airResponseDTOList.get(i).getSido_name(), airQualityRealTimeList.get(i).getSido_name());
            assertEquals(airResponseDTOList.get(i).getStation_name(), airQualityRealTimeList.get(i).getStation_name());
            assertEquals(airResponseDTOList.get(i).getSo2_value(), airQualityRealTimeList.get(i).getSo2_value());
            assertEquals(airResponseDTOList.get(i).getCo_value(), airQualityRealTimeList.get(i).getCo_value());
            assertEquals(airResponseDTOList.get(i).getO3_value(), airQualityRealTimeList.get(i).getO3_value());
            assertEquals(airResponseDTOList.get(i).getNo2_value(), airQualityRealTimeList.get(i).getNo2_value());
            assertEquals(airResponseDTOList.get(i).getPm10_value(), airQualityRealTimeList.get(i).getPm10_value());
            assertEquals(airResponseDTOList.get(i).getPm25_value(), airQualityRealTimeList.get(i).getPm25_value());
            assertEquals(airResponseDTOList.get(i).getKhai_value(), airQualityRealTimeList.get(i).getKhai_value());
            assertEquals(airResponseDTOList.get(i).getKhai_grade(), airQualityRealTimeList.get(i).getKhai_grade());
            assertEquals(airResponseDTOList.get(i).getSo2_grade(), airQualityRealTimeList.get(i).getSo2_grade());
            assertEquals(airResponseDTOList.get(i).getCo_grade(), airQualityRealTimeList.get(i).getCo_grade());
            assertEquals(airResponseDTOList.get(i).getO3_grade(), airQualityRealTimeList.get(i).getO3_grade());
            assertEquals(airResponseDTOList.get(i).getNo2_grade(), airQualityRealTimeList.get(i).getNo2_grade());
            assertEquals(airResponseDTOList.get(i).getPm10_grade(), airQualityRealTimeList.get(i).getPm10_grade());
            assertEquals(airResponseDTOList.get(i).getPm25_grade(), airQualityRealTimeList.get(i).getPm25_grade());
            assertEquals(airResponseDTOList.get(i).getData_time(), airQualityRealTimeList.get(i).getData_time());
        }
    }
}
