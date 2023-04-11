package io.howstheairtoday.airqualityclient.client;

import io.howstheairtoday.airqualityclient.config.OpenApiConfig;
import io.howstheairtoday.airqualityclient.dto.request.PMForecastRequest;
import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import io.howstheairtoday.airqualityclient.exception.ApiDataParsingException;
import io.howstheairtoday.airqualityclient.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PMForecastApiClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(PMForecastApiClient.class);

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private final RestTemplate restTemplate;
    private final OpenApiConfig openApiConfig;

    /**
     * 대기질 예보통보 조회
     *
     * @param searchDate 조회 날짜
     * @return 대기질 예보통보 조회 결과
     */
    public List<PMForecastResponse> fetchPMForecastData(LocalDate searchDate) {

        try {
            LOGGER.info("<대기질 예보통보 조회 시작> 조회 날짜: {}", searchDate);

            PMForecastRequest request = createPMForecastRequest(searchDate);
            String requestUrl = buildRequestUrl(request);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    });

            String response = responseEntity.getBody();

            LOGGER.info("<대기질 예보통보 조회 완료> 조회 날짜: {}", searchDate);

            return parsePMForecastJson(response);
        } catch (Exception exception) {
            throw new ApiRequestException("대기질 예보통보 조회 실패", exception);
        }
    }

    /**
     * 대기질 예보통보 조회 요청 DTO 생성
     *
     * @param searchDate 조회 날짜
     * @return 대기질 예보통보 조회 요청 DTO
     */
    protected PMForecastRequest createPMForecastRequest(LocalDate searchDate) {

        String serviceKey = openApiConfig.getAirServiceKey();
        String returnType = "json";
        int numOfRows = 100;
        int pageNo = 1;
        String informCode = "PM10";

        return PMForecastRequest.builder()
                .serviceKey(serviceKey)
                .returnType(returnType)
                .numOfRows(numOfRows)
                .pageNo(pageNo)
                .searchDate(searchDate)
                .informCode(informCode)
                .build();
    }

    /**
     * 대기질 예보통보 조회 요청 URL 생성
     *
     * @param request 대기질 예보통보 조회 요청 DTO
     * @return 대기질 예보통보 조회 요청 URL
     */
    protected String buildRequestUrl(PMForecastRequest request) {

        String callbackUrl = openApiConfig.getAirPollutionUrl();

        return callbackUrl + buildQueryString(request);
    }

    /**
     * 대기질 예보통보 조회 요청 쿼리 스트링 생성
     *
     * @param request 대기질 예보통보 조회 요청 DTO
     * @return 대기질 예보통보 조회 요청 쿼리 스트링
     */
    private String buildQueryString(PMForecastRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String searchDate = request.getSearchDate().format(formatter);

        return String.format("?serviceKey=%s&returnType=%s&numOfRows=%d&pageNo=%d&searchDate=%s&informCode=%s",
                request.getServiceKey(),
                request.getReturnType(),
                request.getNumOfRows(),
                request.getPageNo(),
                searchDate,
                request.getInformCode());
    }

    /**
     * JSON 데이터를 PMForecastResponse 객체 리스트로 변환
     *
     * @param response JSON 데이터
     * @return PMForecastResponse 객체 리스트
     */
    private List<PMForecastResponse> parsePMForecastJson(String response) {

        try {
            JSONArray items = extractItemsFromResponse(response);

            return parseItemsToPMForecastResponse(items);
        } catch (JSONException exception) {
            LOGGER.error("대기질 예보통보 조회 결과 파싱 실패", exception);
            throw new ApiDataParsingException("대기질 예보통보 조회 결과 파싱 실패", exception);
        }
    }

    /**
     * JSON 데이터에서 items 배열 추출
     *
     * @param response JSON 데이터
     * @return items 배열
     * @throws JSONException JSON 파싱 예외
     */
    private JSONArray extractItemsFromResponse(String response) throws JSONException {

        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");

        return body.getJSONArray("items");
    }

    /**
     * items 배열을 PMForecastResponse 객체 리스트로 변환
     *
     * @param items items 배열
     * @return PMForecastResponse 객체 리스트
     * @throws JSONException JSON 파싱 예외
     */
    private List<PMForecastResponse> parseItemsToPMForecastResponse(JSONArray items) throws JSONException {

        List<PMForecastResponse> pmForecastResponseList = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            PMForecastResponse pmForecastResponse = fromJsonToPMForecastResponse(item);
            pmForecastResponseList.add(pmForecastResponse);
        }

        return pmForecastResponseList;
    }

    /**
     * JSON 데이터를 PMForecastResponse 객체로 변환
     *
     * @param item JSON 데이터
     * @return PMForecastResponse 객체
     */
    private PMForecastResponse fromJsonToPMForecastResponse(JSONObject item) {

        return PMForecastResponse.builder()
                .informCode(item.optString("informCode", ""))
                .informGrade(item.optString("informGrade", ""))
                .informOverall(item.optString("informOverall", ""))
                .informCause(item.optString("informCause", ""))
                .actionKnack(item.optString("actionKnack", ""))
                .informData(item.optString("informData", ""))
                .dataTime(item.optString("dataTime", ""))
                .build();
    }
}
