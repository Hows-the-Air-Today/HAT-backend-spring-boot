package io.howstheairtoday.appairqualityexternalapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/tm")
    @ResponseBody
    public Map<String, CurrentDustResponseDTO> selectAirQualityRealTime(@RequestParam("tmX") String tmX, @RequestParam("tmY") String tmY) {

        System.out.println(tmX + " + " + tmY);
        // TM 좌표를 이용하여 근처 측정소 찾기
        String stationName = airQualityRealTimeService.getNear(tmX, tmY);

        // 데이터 값을 Map 으로 변환
        Map<String, CurrentDustResponseDTO> data = new HashMap<>();
        data.put("airQuality", airQualityRealTimeService.selectAirQualityRealTime(stationName));
        System.out.println(data);
        return data;
    }
}
