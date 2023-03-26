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

import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;
import io.howstheairtoday.service.dto.AirResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AirQualityRealTimeService {
    private final AirQualityRealTimeRepository airQualityRealTimeRepository;
    @Value("${air.apikey}") // application.yml에 저장된 서비스키 값
    private String airapiKey;

    // 시도별 대기정보 찾기
    public List<AirResponseDTO> getAirQualityData() {
        String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String queryParams = "?serviceKey=" + airapiKey
            + "&returnType=json"
            + "&numOfRows=642"
            + "&pageNo=1"
            + "&sidoName=전국"
            + "&ver=1.0";
        ResponseEntity<String> response = restTemplate.exchange(url + queryParams, HttpMethod.GET, entity,
            String.class);
        
        // List<AirResponseDTO> airResponseDTOList = StringToDTOList(response.getBody());
        return null;
    }

}
