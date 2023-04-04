package io.howstheairtoday.appairqualityexternalapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.appairqualityexternalapi.service.ExternalApiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airquality")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    // 대기질 측정정보 조회
    @GetMapping("/stationName/{umdName}")
    @ResponseBody
    public Map<String, CurrentDustResponseDTO> selectAirQualityRealTime(@PathVariable("umdName") String umdName){
        System.out.println(umdName);
        // 근처 측정소 찾기
        String stationName = externalApiService.getNear(umdName);
        System.out.println(stationName);
        // 데이터 값을 Map 으로 변환
        Map<String, CurrentDustResponseDTO> data = new HashMap<>();
        data.put("airQuality", externalApiService.selectAirQualityRealTime(stationName));
        return data;
    }
}
