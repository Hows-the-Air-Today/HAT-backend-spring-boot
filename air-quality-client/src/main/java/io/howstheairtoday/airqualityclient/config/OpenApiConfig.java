package io.howstheairtoday.airqualityclient.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OpenApiConfig {

    // Open API 서비스 키
    @Value("${air.serviceKey}")
    private String airServiceKey;

    // 대기질 예보통보 조회 URL
    @Value("${air.airPollutionUrl}")
    private String airPollutionUrl;
}
