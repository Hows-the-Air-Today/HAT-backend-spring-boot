package io.howstheairtoday.appairqualityexternalapi.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ExternalApiService {
    @Value("${air.informationkey}")
    private String informationkey;

    //TM 좌표 찾아오기
    public List<String> getTM(String umdName) {
        String url = "https://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getTMStdrCrdnt";

        //RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String queryParams = "?serviceKey=" + informationkey
            + "&returnType=json"
            + "&numOfRows=100"
            + "&pageNo=1"
            + "&umdName=" + umdName;
        ResponseEntity<String> response = restTemplate.exchange(url + queryParams, HttpMethod.GET, entity,
            String.class);

        //JSON 파싱
        JSONArray items = JsonToString(response.getBody());
        JSONObject item = items.getJSONObject(0);

        //tmX, tmY 좌표
        String tmX = item.getString("tmX");
        String tmY = item.getString("tmY");

        List<String> tm = new ArrayList<>();
        tm.add(tmX);
        tm.add(tmY);

        return tm;
    }

    //TM 좌표를 통해 근접 측정소 찾기
    public String getNear(String umdName) {
        String url = "https://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList";

        // 임의의 위치
        List<String> tm;
        tm = getTM(umdName);
        String tmX = tm.get(0);
        String tmY = tm.get(1);

        //RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String queryParams = "?serviceKey=" + informationkey
            + "&returnType=json"
            + "&tmX=" + tmX
            + "&tmY=" + tmY
            + "&ver=1.1";
        ResponseEntity<String> response = restTemplate.exchange(url + queryParams, HttpMethod.GET, entity,
            String.class);

        //JSON 파싱
        JSONArray items = JsonToString(response.getBody());
        JSONObject item = items.getJSONObject(0);

        //측정소명
        String stationName = item.getString("stationName");
        return stationName;
    }

    public JSONArray JsonToString(String response) {
        JSONObject root = new JSONObject(response);
        JSONObject res = root.getJSONObject("response");
        JSONObject body = res.getJSONObject("body");
        JSONArray items = body.getJSONArray("items");
        return items;
    }
}
