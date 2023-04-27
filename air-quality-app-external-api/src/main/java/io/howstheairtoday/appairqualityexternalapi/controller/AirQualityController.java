package io.howstheairtoday.appairqualityexternalapi.controller;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.appairqualityexternalapi.common.ApiResponse;
import io.howstheairtoday.appairqualityexternalapi.service.AirQualityRealTimeService;
import io.howstheairtoday.appairqualityexternalapi.service.PMForecastService;
import io.howstheairtoday.appairqualityexternalapi.service.dto.response.PMForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airquality")
public class AirQualityController {

    private final AirQualityRealTimeService airQualityRealTimeService;
    private final PMForecastService pmForecastService;

    // 대기질 측정정보 조회
    @GetMapping("/tm")
    @ResponseBody
    public Map<String, CurrentDustResponseDTO> selectAirQualityRealTime(@RequestParam("tmx") String tmX, @RequestParam("tmy") String tmY) {

        // TM 좌표를 이용하여 근처 측정소 찾기
        String stationName = airQualityRealTimeService.getNear(tmX, tmY);

        // 데이터 값을 Map 으로 변환
        Map<String, CurrentDustResponseDTO> data = new HashMap<>();
        data.put("airQuality", airQualityRealTimeService.selectAirQualityRealTime(stationName));
        return data;
    }

    @GetMapping("/forecast")
    public ApiResponse<PMForecastResponse> getAllPMForecasts() {

        PMForecastResponse pmForecastResponse = pmForecastService.getAllPMForecastData();

        return ApiResponse.success("미세먼지/초미세먼지 주간 예보 정보 조회 성공", pmForecastResponse);
    }

    @GetMapping("/ranking")
    public Map<String, List<CurrentDustResponseDTO>> getRanking() {
        List<CurrentDustResponseDTO> bestList = airQualityRealTimeService.findBest10();
        List<CurrentDustResponseDTO> worstList = airQualityRealTimeService.findWorst10();

        List<CurrentDustResponseDTO> combinedList = new ArrayList<>();
        combinedList.addAll(bestList);
        combinedList.addAll(worstList);

        Map<String, List<CurrentDustResponseDTO>> data = new HashMap<>();
        data.put("ranking", combinedList);

        return data;
    }
}
