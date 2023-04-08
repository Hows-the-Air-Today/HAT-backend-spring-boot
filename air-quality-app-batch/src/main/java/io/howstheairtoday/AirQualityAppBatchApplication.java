package io.howstheairtoday;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO: 리팩토링(service 정렬 메서드)
@SpringBootApplication
@EnableJpaAuditing
// 지우면 시작할 때 배치 시작 처음 테이블 생성시 지워줘야함
@EnableBatchProcessing
public class AirQualityAppBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirQualityAppBatchApplication.class, args);
    }

}
