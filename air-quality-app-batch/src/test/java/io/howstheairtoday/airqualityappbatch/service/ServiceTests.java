package io.howstheairtoday.airqualityappbatch.service;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@SpringBootTest
public class ServiceTests {

    @Value("${air.apikey}") // application.yml에 저장된 서비스키 값
    private String airapiKey;

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

}
