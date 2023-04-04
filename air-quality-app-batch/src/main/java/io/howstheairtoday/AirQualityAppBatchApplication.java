package io.howstheairtoday;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 리팩토링(service 정렬 메서드)
@SpringBootApplication
@EnableBatchProcessing
public class AirQualityAppBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirQualityAppBatchApplication.class, args);
    }

}
