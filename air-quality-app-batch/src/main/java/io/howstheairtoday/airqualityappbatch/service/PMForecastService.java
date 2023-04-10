package io.howstheairtoday.airqualityappbatch.service;

import io.howstheairtoday.airqualityappbatch.config.OpenApiConfig;
import io.howstheairtoday.airqualityappbatch.service.dto.request.PMForecastRequest;
import io.howstheairtoday.airqualityappbatch.service.dto.response.PMForecastResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PMForecastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PMForecastService.class);
    private final OpenApiConfig openApiConfig;

    @Autowired
    public PMForecastService(OpenApiConfig openApiConfig) {

        this.openApiConfig = openApiConfig;
    }

    // 대기질 예보통보 데이터 조회
    public List<PMForecastResponse> getPMForecastData(LocalDate searchDate) {

        PMForecastRequest request = createPMForecastRequest(searchDate);
        String requestUrl = buildRequestUrl(request);
        List<PMForecastResponse> response = requestPMForecast(requestUrl);

        LOGGER.info("대기질 예보통보 데이터 조회: {}", response);

        return response;
    }

    // 대기질 예보통보 조회 요청 DTO 생성
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

    // 대기질 예보통보 조회 요청 URL 생성
    protected String buildRequestUrl(PMForecastRequest request) {

        String callbackUrl = openApiConfig.getAirPollutionUrl();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String searchDateString = request.getSearchDate().format(formatter);

        return String.format("%s?serviceKey=%s&returnType=%s&numOfRows=%d&pageNo=%d&searchDate=%s&informCode=%s",
                callbackUrl,
                request.getServiceKey(),
                request.getReturnType(),
                request.getNumOfRows(),
                request.getPageNo(),
                searchDateString,
                request.getInformCode());
    }

    // 대기질 예보통보 조회 API 호출
    private List<PMForecastResponse> requestPMForecast(String requestUrl) {

        List<PMForecastResponse> pmForecastResponseList = new ArrayList<>();

        try {
            HttpURLConnection connection = createConnection(requestUrl);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                pmForecastResponseList = parsePMForecastResponse(response);

                LOGGER.info("대기질 예보통보 조회 성공: {}", response);
            }

            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                LOGGER.error("대기질 예보통보 조회 실패: {}", responseCode);
            }
        } catch (Exception e) {
            LOGGER.error("API 호출 실패: {}", e.getMessage());
        }

        return pmForecastResponseList;
    }

    // HTTP 연결 생성
    protected HttpURLConnection createConnection(String requestUrl) throws IOException {

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

    // HTTP 응답 읽기
    protected String readResponse(HttpURLConnection connection) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        return response.toString();
    }

    // 대기질 예보통보 조회 응답 파싱
    private List<PMForecastResponse> parsePMForecastResponse(String response) {
        List<PMForecastResponse> pmForecastResponseList = new ArrayList<>();

        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            PMForecastResponse pmForecastResponse = PMForecastResponse.builder()
                    .informCode(item.optString("informCode", ""))
                    .informGrade(item.optString("informGrade", ""))
                    .informOverall(item.optString("informOverall", ""))
                    .informCause(item.optString("informCause", ""))
                    .actionKnack(item.optString("actionKnack", ""))
                    .informData(item.optString("informData", ""))
                    .dateTime(item.optString("dateTime", ""))
                    .build();

            pmForecastResponseList.add(pmForecastResponse);
        }

        return pmForecastResponseList;
    }
}
