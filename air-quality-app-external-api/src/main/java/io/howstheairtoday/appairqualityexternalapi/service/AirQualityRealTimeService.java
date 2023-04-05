package io.howstheairtoday.appairqualityexternalapi.service;

import java.util.ArrayList;
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

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirQualityRealTimeService {

    // TM 좌표 및 근처 측정소 정보를 찾기 위한 API 키
    @Value("${air.informationkey}")
    private String informationKey;

    // 공공데이터 포털에서 TM좌표를 받아오기 위한 url
    @Value("${air.tmUrl}")
    private String tmUrl;

    // 공공데이터 포털에서 근처 측정소 위치를 받아오기 위한 url
    @Value("${air.nearUrl}")
    private String nearUrl;

    // TM 좌표 찾아오기
    public List<String> getTM(final String umdName) {

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        // url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + informationKey
            + "&returnType=json"
            + "&numOfRows=100"
            + "&pageNo=1"
            + "&umdName=" + umdName;

        ResponseEntity<String> response = restTemplate.exchange(tmUrl + queryParams, HttpMethod.GET, entity,
            String.class);

        // JSON 객체에 있는 값을 사용하기 위한 작업
        JSONArray items = JsonToString(response.getBody());
        JSONObject item = items.getJSONObject(0);

        // tmX, tmY 좌표
        String tmX = item.getString("tmX");
        String tmY = item.getString("tmY");

        List<String> tm = new ArrayList<>();
        tm.add(tmX);
        tm.add(tmY);

        return tm;
    }

    // TM 좌표를 통해 근접 측정소 찾기
    public String getNear(final String umdName) {

        // 위치 배열을 문자열 변수로 변경
        List<String> tm;
        tm = getTM(umdName);
        String tmX = tm.get(0);
        String tmY = tm.get(1);

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        // url 뒤에 붙일 내용들을 String으로 정의
        String queryParams = "?serviceKey=" + informationKey
            + "&returnType=json"
            + "&tmX=" + tmX
            + "&tmY=" + tmY
            + "&ver=1.1";

        ResponseEntity<String> response = restTemplate.exchange(nearUrl + queryParams, HttpMethod.GET, entity,
            String.class);

        // JSON 파싱
        JSONArray items = JsonToString(response.getBody());

        JSONObject item = null;

        // 측정소명에 umdName이 포함된 item을 찾아서 stationName에 저장
        for (int i = 0; i < items.length(); i++) {
            item = items.getJSONObject(i);
            String addr = item.getString("addr");
            if (addr.contains(umdName)) {
                break;
            }
        }

        //측정소명
        return item.getString("stationName");
    }

    // JSON 파싱을 위한 메서드 선언
    public JSONArray JsonToString(String response) {
        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        return items;
    }

    private final AirQualityRealTimeRepository airQualityRealTimeRepository;

    // 실시간 대기정보 조회
    public CurrentDustResponseDTO selectAirQualityRealTime(String stationName) {

        AirQualityRealTime airQualityRealTime = airQualityRealTimeRepository.findAirQualityRealTimeByStationName(stationName);

        return entityToDTO(airQualityRealTime);
    }

    // Entity를 DTO로 변환해주는 메서드
    public CurrentDustResponseDTO entityToDTO(AirQualityRealTime airQualityRealTime){

        return CurrentDustResponseDTO.builder()
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
    }
}
