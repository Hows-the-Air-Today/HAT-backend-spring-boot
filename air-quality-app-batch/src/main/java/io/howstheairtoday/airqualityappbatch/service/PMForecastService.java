package io.howstheairtoday.airqualityappbatch.service;

import io.howstheairtoday.airqualityclient.client.PMForecastApiClient;
import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PMForecastService {

    private final PMForecastApiClient pmForecastApiClient;

    /**
     * 대기질 예보통보 조회
     *
     * @param searchDate 조회 날짜
     * @param informCode 통보 코드
     * @return 대기질 예보통보 조회 결과
     */
    public List<PMForecastResponse> getPMForecastData(LocalDate searchDate, String informCode) {

        return pmForecastApiClient.fetchPMForecastData(searchDate, informCode);
    }
}
