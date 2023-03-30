package io.howstheairtoday.airqualityappbatch.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

import io.howstheairtoday.service.dto.response.CurrentDustResponseDTO;

@ActiveProfiles("test")
@SpringBootTest
public class ServiceTests {

    // 시도별 측정 정보 호출 시에 사용될 API Key
    @Value("${air.apikey}")
    private String airApiKey;

    // 공공데이터 포털에서 시도별 대기 정보를 받아오기 위한 url
    @Value("${air.sidoUrl}")
    private String url;

    @DisplayName("시도별 대기 정보를 찾는 API 호출")
    @Test
    public void apiTest() {

        // Given

        // RestTemplate를 통한 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // When
        // url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + airApiKey
            + "&returnType=json"
            + "&numOfRows=642"
            + "&pageNo=1"
            + "&sidoName=전국"
            + "&ver=1.0";

        // restTemplate를 통한 API 호출
        ResponseEntity<String> response = restTemplate.exchange(url + queryParams, HttpMethod.GET, entity,
            String.class);

        // Then

        // JSON 객체에 있는 값을 사용하기 위한 작업
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
        JSONObject root = new JSONObject(response.getBody());
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);

        // 측정소명
        String sidoName = item.optString("sidoName");
        String stationName = item.optString("stationName");
        String so2Value = item.optString("so2Value");
        String coValue = item.optString("coValue");
        String o3Value = item.optString("o3Value");
        String no2Value = item.optString("no2Value");
        String pm10Value = item.optString("pm10Value");
        String pm25Value = item.optString("pm25Value");
        int khaiValue;
        // Int 타입으로 변환 중 숫자가 아닌 문자가 있으면 나올 수 없는 수로 처리
        try{
            khaiValue = Integer.parseInt(item.optString("khaiValue"));
        }catch (NumberFormatException e){
            khaiValue = -1;
        }
        String khaiGrade = item.optString("khaiGrade");
        String so2Grade = item.optString("so2Grade");
        String coGrade = item.optString("coGrade");
        String o3Grade = item.optString("o3Grade");
        String no2Grade = item.optString("no2Grade");
        String pm10Grade = item.optString("pm10Grade");
        String pm25Grade = item.optString("pm25Grade");
        String dataTime = item.optString("dataTime");

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
        assertNotNull(dataTime);
    }

    @DisplayName("데이터 가공 테스트")
    @Test
    void testStringToDTOList() {

        //Given
        // 예제 API JSON 데이터
        String response = "{ \"response\": { \"body\": { \"items\": [ { \"sidoName\": \"서울\", \"stationName\": \"종로구\", \"dataTime\": \"2022-03-24 14:00\", \"so2Value\": \"0.003\", \"coValue\": \"0.4\", \"o3Value\": \"0.027\", \"no2Value\": \"0.014\", \"pm10Value\": \"37\", \"pm25Value\": \"21\", \"khaiValue\": \"64\", \"khaiGrade\": \"2\", \"so2Grade\": \"1\", \"coGrade\": \"2\", \"o3Grade\": \"2\", \"no2Grade\": \"1\", \"pm10Grade\": \"1\", \"pm25Grade\": \"2\" },  { \"sidoName\": \"서울\", \"stationName\": \"서초구\", \"dataTime\": \"2022-03-24 14:00\", \"so2Value\": \"0.003\", \"coValue\": \"0.4\", \"o3Value\": \"0.027\", \"no2Value\": \"0.014\", \"pm10Value\": \"37\", \"pm25Value\": \"21\", \"khaiValue\": \"65\", \"khaiGrade\": \"2\", \"so2Grade\": \"1\", \"coGrade\": \"2\", \"o3Grade\": \"2\", \"no2Grade\": \"1\", \"pm10Grade\": \"1\", \"pm25Grade\": \"2\" } ] } } }";

        // JSON 데이터 파싱
        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");

        // StringToDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<CurrentDustResponseDTO> currentDustResponseDTOList = new ArrayList<>();
        int khaiValue;
        LocalDateTime dateTime = null;

        // When
        // 반복문을 통해 리스트에 저장
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (!item.optString("dataTime").isEmpty()) {
                dateTime = LocalDateTime.parse(item.getString("dataTime"), formatter);
            }
            // khaiVaule의 정렬을 위해 Int 타입으로 변환 중 숫자가 아닌 문자가 있으면 나올 수 없는 수로 처리
            try{
                khaiValue = Integer.parseInt(item.optString("khaiValue"));
            }catch (NumberFormatException e){
                khaiValue = -1;
            }

            CurrentDustResponseDTO currentDustResponseDTO = CurrentDustResponseDTO.builder()
                .sidoName(item.getString("sidoName"))
                .stationName(item.getString("stationName"))
                .so2Value(item.optString("so2Value"))
                .coValue(item.optString("coValue"))
                .o3Value(item.optString("o3Value"))
                .no2Value(item.optString("no2Value"))
                .pm10Value(item.optString("pm10Value"))
                .pm25Value(item.optString("pm25Value"))
                .khaiValue(khaiValue)
                .khaiGrade(item.optString("khaiGrade"))
                .so2Grade(item.optString("so2Grade"))
                .coGrade(item.optString("coGrade"))
                .o3Grade(item.optString("o3Grade"))
                .no2Grade(item.optString("no2Grade"))
                .pm10Grade(item.optString("pm10Grade"))
                .pm25Grade(item.optString("pm25Grade"))
                .dataTime(dateTime)
                .build();
            currentDustResponseDTOList.add(currentDustResponseDTO);
        }

        // KhaiValue를 기준으로 정렬
        currentDustResponseDTOList.sort(Comparator.comparing(CurrentDustResponseDTO::getKhaiValue));

        // Then
        // 비교
        assertNotNull(currentDustResponseDTOList);
        assertEquals("서울", currentDustResponseDTOList.get(0).getSidoName());
        assertEquals("종로구", currentDustResponseDTOList.get(0).getStationName());
        assertEquals("2022-03-24T14:00", currentDustResponseDTOList.get(0).getDataTime().toString());
        assertEquals("0.003", currentDustResponseDTOList.get(0).getSo2Value());
        assertEquals("0.4", currentDustResponseDTOList.get(0).getCoValue());
        assertEquals("0.027", currentDustResponseDTOList.get(0).getO3Value());
        assertEquals("0.014", currentDustResponseDTOList.get(0).getNo2Value());
        assertEquals("37", currentDustResponseDTOList.get(0).getPm10Value());
        assertEquals("21", currentDustResponseDTOList.get(0).getPm25Value());
        assertEquals(64, currentDustResponseDTOList.get(0).getKhaiValue());
        assertEquals("2", currentDustResponseDTOList.get(0).getKhaiGrade());
        assertEquals("1", currentDustResponseDTOList.get(0).getSo2Grade());
        assertEquals("2", currentDustResponseDTOList.get(0).getCoGrade());
        assertEquals("2", currentDustResponseDTOList.get(0).getO3Grade());
        assertEquals("1", currentDustResponseDTOList.get(0).getNo2Grade());
        assertEquals("1", currentDustResponseDTOList.get(0).getPm10Grade());
        assertEquals("2", currentDustResponseDTOList.get(0).getPm25Grade());
    }

}
