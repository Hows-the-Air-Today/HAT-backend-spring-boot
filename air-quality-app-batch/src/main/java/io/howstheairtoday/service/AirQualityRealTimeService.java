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
import org.springframework.web.client.RestTemplate;

import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;
import io.howstheairtoday.service.dto.AirResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AirQualityRealTimeService {
    private final AirQualityRealTimeRepository airQualityRealTimeRepository;
    @Value("${air.apikey}") // application.yml에 저장된 서비스키 값
    private String airapiKey;

    // 시도별 대기정보 찾기
    public List<AirResponseDTO> getAirQualityData() {
        //공공데이터 포털에서 시도별 대기 정보를 받아오기 위한 url
        String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

        //RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

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
        List<AirResponseDTO> airResponseDTOList = StringToDTOList(response.getBody());
        return airResponseDTOList;
    }

    //시도별 대기 정보 전국 데이터 가공(khaiValue 순으로 정렬)
    public List<AirResponseDTO> StringToDTOList(String response) {
        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<AirResponseDTO> airResponseDTOList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            LocalDateTime dateTime = null;
            if (!item.optString("dataTime").isEmpty()) {
                dateTime = LocalDateTime.parse(item.getString("dataTime"), formatter);
            }
            AirResponseDTO airResponseDTO = AirResponseDTO.builder()
                .sido_name(item.getString("sidoName"))
                .station_name(item.getString("stationName"))
                .so2_value(item.optString("so2Value"))
                .co_value(item.optString("coValue"))
                .o3_value(item.optString("o3Value"))
                .no2_value(item.optString("no2Value"))
                .pm10_value(item.optString("pm10Value"))
                .pm25_value(item.optString("pm25Value"))
                .khai_value(item.optString("khaiValue"))
                .khai_grade(item.optString("khaiGrade"))
                .so2_grade(item.optString("so2Grade"))
                .co_grade(item.optString("coGrade"))
                .o3_grade(item.optString("o3Grade"))
                .no2_grade(item.optString("no2Grade"))
                .pm10_grade(item.optString("pm10Grade"))
                .pm25_grade(item.optString("pm25Grade"))
                .data_time(dateTime)
                .build();
            airResponseDTOList.add(airResponseDTO);
        }
        airResponseDTOList.sort(Comparator.comparing(AirResponseDTO::getKhai_value));
        return airResponseDTOList;
    }

}
