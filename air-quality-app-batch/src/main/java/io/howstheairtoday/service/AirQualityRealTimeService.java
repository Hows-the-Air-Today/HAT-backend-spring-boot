package io.howstheairtoday.service;

import java.util.List;

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
        // List<AirResponseDTO> airResponseDTOList = StringToDTOList(response.getBody());
        return null;
    }

}
