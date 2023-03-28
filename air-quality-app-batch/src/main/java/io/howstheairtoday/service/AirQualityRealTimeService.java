package io.howstheairtoday.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.howstheairtoday.service.dto.response.CurrentDustResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirQualityRealTimeService {

    // 이후에 데이터 저장때 사용될 Repository
    // private final AirQualityRealTimeRepository airQualityRealTimeRepository;
    // 시도별 측정 정보 호출 시에 사용될 API Key
    @Value("${air.apikey}")
    private String airApiKey;

    // 공공데이터 포털에서 시도별 대기 정보를 받아오기 위한 url
    @Value("${air.sidoUrl}")
    private String url;

    // 시도별 대기정보 찾기
    public List<CurrentDustResponseDTO> getAirQualityData() {

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

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

        List<CurrentDustResponseDTO> currentDustResponseDTOList = StringToDTOList(response.getBody());
        return currentDustResponseDTOList;
    }

    // 시도별 대기 정보 전국 데이터 가공(khaiValue 순으로 정렬)
    public List<CurrentDustResponseDTO> StringToDTOList(String response) {

        // JSON 데이터 파싱
        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<CurrentDustResponseDTO> currentDustResponseDTOList = new ArrayList<>();

        // 반복문을 통해 리스트에 저장
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            LocalDateTime dateTime = null;
            if (!item.optString("dataTime").isEmpty()) {
                // StringToDateTime
                dateTime = LocalDateTime.parse(item.getString("dataTime"), formatter);
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
                .khaiValue(item.optString("khaiValue"))
                .khaiGrade(item.optString("khaiGrade"))
                .so2Grade(item.optString("so2Grade"))
                .coGrade(item.optString("coGrade"))
                .o3Grade(item.optString("o3Grade"))
                .no2Grade(item.optString("no2Grade"))
                .pm10Grade(item.optString("pm10Grade"))
                .pm25Grade(item.optString("pm25Grade"))
                .dataTime(dateTime)
                .build();

            // KhaiValue를 기준으로 정렬
            currentDustResponseDTOList.add(currentDustResponseDTO);
        }
        currentDustResponseDTOList.sort(Comparator.comparing(CurrentDustResponseDTO::getKhaiValue));
        return currentDustResponseDTOList;
    }

}
