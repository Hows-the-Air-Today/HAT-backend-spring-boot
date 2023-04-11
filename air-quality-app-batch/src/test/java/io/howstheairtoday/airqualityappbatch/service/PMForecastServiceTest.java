package io.howstheairtoday.airqualityappbatch.service;

import io.howstheairtoday.airqualityclient.config.OpenApiConfig;
import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("대기질 예보 통보 조회 서비스 테스트")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PMForecastServiceTest {

    @Autowired
    private PMForecastService pmForecastService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenApiConfig openApiConfig;

    private MockRestServiceServer mockServer;
    private LocalDate searchDate;

    @BeforeEach
    void setUp() throws IOException {

        searchDate = LocalDate.of(2023, 3, 31);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mockedPMForecastResponse.json");
        String responseBody = new String(inputStream.readAllBytes());

        // Mocking(실제 API 호출 없이 테스트)
        mockServer = MockRestServiceServer.createServer(restTemplate);

        String requestUrl = UriComponentsBuilder.fromHttpUrl(openApiConfig.getAirPollutionUrl())
                .queryParam("serviceKey", openApiConfig.getAirServiceKey())
                .queryParam("returnType", "json")
                .queryParam("numOfRows", 100)
                .queryParam("pageNo", 1)
                .queryParam("searchDate", searchDate)
                .queryParam("informCode", "PM10")
                .toUriString();

        // 가상의 요청, 응답 정의
        mockServer.expect(requestTo(requestUrl))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }

    @DisplayName("대기질 예보 통보 조회")
    @Test
    void shouldReturnPMForecastData() {

        // given
        PMForecastResponse expectedResponse = createExpectedResponse()
                .informCode("PM10")
                .informGrade("서울 : 나쁨,제주 : 보통,전남 : 보통,전북 : 보통,광주 : 보통,경남 : 보통,경북 : 보통,울산 : 보통,대구 : 보통,부산 : 보통,충남 : 나쁨,충북 : 보통,세종 : 보통,대전 : 보통,영동 : 보통,영서 : 보통,경기남부 : 나쁨,경기북부 : 나쁨,인천 : 나쁨")
                .informOverall("○ [미세먼지] 수도권·충청권은 '나쁨', 그 밖의 권역은 '보통'으로 예상됩니다. 다만, 강원영동·전북·대구·울산·경북은 밤에 '나쁨' 수준일 것으로 예상됩니다.")
                .informCause("○ [미세먼지] 대부분 중부지역은 전일 미세먼지 잔류하고, 상층으로 국외 미세먼지가 추가적으로 유입되어 농도가 높을 것으로 예상됩니다.")
                .actionKnack("")
                .informData("2023-03-31")
                .dataTime("2023-03-31 23시 발표")
                .build();

        // when
        List<PMForecastResponse> actualJsonResponse = pmForecastService.getPMForecastData(searchDate);

        // then
        mockServer.verify();
        assertThat(actualJsonResponse)
                .isNotNull()
                .hasSize(20)
                .usingRecursiveFieldByFieldElementComparator().contains(expectedResponse);
    }

    /**
     * 대기질 예보 통보 조회 결과 생성 메서드
     *
     * @return 대기질 예보 통보 조회 결과
     */
    private PMForecastResponse.PMForecastResponseBuilder createExpectedResponse() {

        return PMForecastResponse.builder();
    }
}
