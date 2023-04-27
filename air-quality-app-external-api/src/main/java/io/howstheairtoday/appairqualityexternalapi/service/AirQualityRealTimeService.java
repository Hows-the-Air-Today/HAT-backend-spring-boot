package io.howstheairtoday.appairqualityexternalapi.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // 공공데이터 포털에서 근처 측정소 위치를 받아오기 위한 url
    @Value("${air.nearUrl}")
    private String nearUrl;

    // TM 좌표를 통해 근접 측정소 찾기
    public String getNear(final String tmX, final String tmY) {

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
        JSONObject item = items.getJSONObject(0);

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

    // 지역 랭킹 정보를 받아옴
    public List<CurrentDustResponseDTO> findBest10() {
        Pageable pageable = PageRequest.of(0, 10);

        List<AirQualityRealTime> airQualityRealTimeList = airQualityRealTimeRepository.findBest10(pageable);
        List<CurrentDustResponseDTO> currentDustResponseDTOList = new ArrayList<>();
        for (AirQualityRealTime airQualityRealTime : airQualityRealTimeList) {
            currentDustResponseDTOList.add(entityToDTO(airQualityRealTime));
        }
        return currentDustResponseDTOList;
    }

    public List<CurrentDustResponseDTO> findWorst10() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AirQualityRealTime> airQualityRealTimeList = airQualityRealTimeRepository.findWorst10(pageable);
        List<CurrentDustResponseDTO> currentDustResponseDTOList = new ArrayList<>();
        for (AirQualityRealTime airQualityRealTime : airQualityRealTimeList) {
            currentDustResponseDTOList.add(entityToDTO(airQualityRealTime));
        }
        return currentDustResponseDTOList;
    }
}
