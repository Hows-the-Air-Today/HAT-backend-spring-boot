package io.howstheairtoday.appairqualityexternalapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.appairqualityexternalapi.service.AirQualityRealTimeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airquality")
public class AirQualityRealTimeController {

    private final AirQualityRealTimeService airQualityRealTimeService;

    // 대기질 측정정보 조회
    @GetMapping("/stationName/{umdName}")
    @ResponseBody
    public Map<String, CurrentDustResponseDTO> selectAirQualityRealTime(@PathVariable("umdName") String umdName){

        // 근처 측정소 찾기
        String stationName = airQualityRealTimeService.getNear(umdName);

        // 데이터 값을 Map 으로 변환
        Map<String, CurrentDustResponseDTO> data = new HashMap<>();
        data.put("airQuality", airQualityRealTimeService.selectAirQualityRealTime(stationName));
        return data;
    }
}
