package io.howstheairtoday.airqualityappbatch.service;

import io.howstheairtoday.airqualityappbatch.config.OpenApiConfig;
import io.howstheairtoday.airqualityappbatch.service.dto.request.PMForecastRequest;
import io.howstheairtoday.airqualityappbatch.service.dto.response.PMForecastResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("대기질 예보 통보 조회 서비스 테스트")
@SpringBootTest
@ActiveProfiles("test")
class PMForecastServiceTest {

    @Autowired
    private PMForecastService pmForecastService;

    @Autowired
    private OpenApiConfig openApiConfig;
    private LocalDate searchDate;

    @BeforeEach
    void setUp() {

        searchDate = LocalDate.of(2023, 3, 31);
    }

    @DisplayName("대기질 예보 통보 조회")
    @Test
    void shouldRetrivePMForecastData() {

        // given
        PMForecastRequest request = pmForecastService.createPMForecastRequest(searchDate);

        // when
        List<PMForecastResponse> pmForecastResponseList = pmForecastService.getPMForecastData(request.getSearchDate());

        // then
        assertThat(pmForecastResponseList).isNotEmpty();
    }
}