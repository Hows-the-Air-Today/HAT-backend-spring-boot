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
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PMForecastApiClient {

    public static final Logger logger = LoggerFactory.getLogger(PMForecastApiClient.class);

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    private final RestTemplate restTemplate;
    private final OpenApiConfig openApiConfig;

    /**
     * 대기질 예보통보 조회
     *
     * @param searchDate 조회 날짜
     * @param informCode 통보 코드
     * @return 대기질 예보통보 조회 결과
     */
    public List<PMForecastResponse> fetchPMForecastData(LocalDate searchDate, String informCode) {

        logger.info("<대기질 예보통보 조회 시작> 조회 날짜: {}, 통보 코드: {}", searchDate, informCode);

        PMForecastRequest request = createPMForecastRequest(searchDate, informCode);
        URI requestUrl = buildRequestUrl(request);
        HttpEntity<String> httpEntity = createHttpEntity();

        ResponseEntity<String> responseEntity = executeApiRequest(requestUrl, httpEntity);

        String response = responseEntity.getBody();

        logger.info("<대기질 예보통보 조회 완료> 조회 날짜: {}, 통보 코드: {}", searchDate, informCode);

        return parsePMForecastJson(response);
    }

    /**
     * 대기질 예보통보 조회 요청 DTO 생성
     *
     * @param searchDate 조회 날짜
     * @param informCode 통보 코드
     * @return 대기질 예보통보 조회 요청 DTO
     */
    protected PMForecastRequest createPMForecastRequest(LocalDate searchDate, String informCode) {

        String serviceKey = openApiConfig.getAirServiceKey();
        String returnType = "json";
        int numOfRows = 100;
        int pageNo = 1;

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
    protected URI buildRequestUrl(PMForecastRequest request) {

        String callbackUrl = openApiConfig.getAirPollutionUrl();

        return buildUriComponents(request, callbackUrl).toUri();
    }

    /**
     * 대기질 예보통보 조회 요청 URL 파라미터 생성
     *
     * @param request     대기질 예보통보 조회 요청 DTO
     * @param callbackUrl 대기질 예보통보 조회 요청 URL
     * @return 대기질 예보통보 조회 요청 URL
     */
    private UriComponents buildUriComponents(PMForecastRequest request, String callbackUrl) {

        String searchDate = request.getSearchDate().format(DATE_FORMATTER);

        return UriComponentsBuilder.fromHttpUrl(callbackUrl)
                .queryParam("serviceKey", request.getServiceKey())
                .queryParam("returnType", request.getReturnType())
                .queryParam("numOfRows", request.getNumOfRows())
                .queryParam("pageNo", request.getPageNo())
                .queryParam("searchDate", searchDate)
                .queryParam("informCode", request.getInformCode())
                .build(true);
    }

    /**
     * 대기질 예보통보 조회 요청 헤더 생성
     *
     * @return 대기질 예보통보 조회 요청 헤더
     */
    private HttpEntity<String> createHttpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(headers);
    }

    /**
     * 대기질 예보통보 조회 요청 실행
     *
     * @param requestUrl 대기질 예보통보 조회 요청 URL
     * @param entity     대기질 예보통보 조회 요청 헤더
     * @return 대기질 예보통보 조회 응답
     */
    private ResponseEntity<String> executeApiRequest(URI requestUrl, HttpEntity<String> entity) {

        try {
            return restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception exception) {
            throw new ApiRequestException("대기질 예보통보 조회 실패", exception);
        }
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
            logger.error("대기질 예보통보 조회 결과 파싱 실패", exception);
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
