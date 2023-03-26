package io.howstheairtoday.airqualitydomainrds.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {AirQualityRealTimeRepository.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.airqualitydomainrds.repository")
@EntityScan("io.howstheairtoday.airqualitydomainrds.entity")
public class RepositoryTests {
    @Autowired
    private AirQualityRealTimeRepository airQualityRealTimeRepository;

    @DisplayName("받아온 정보 저장")
    @Test
    public void saveTest() {
        //Given
        AirQualityRealTime airQualityRealTime = AirQualityRealTime.builder()
            .air_quality_real_time_measurement_id(1L)
            .sido_name("1")
            .station_name("1")
            .so2_value("1")
            .co_value("1")
            .o3_value("1")
            .no2_value("1")
            .pm10_value("1")
            .pm25_value("1")
            .khai_value("1")
            .khai_grade("1")
            .so2_grade("1")
            .co_grade("1")
            .o3_grade("1")
            .no2_grade("1")
            .pm10_grade("1")
            .pm25_grade("1")
            .build();

        // When
        airQualityRealTimeRepository.save(airQualityRealTime);
        AirQualityRealTime saved = airQualityRealTimeRepository.findById(
            airQualityRealTime.getAir_quality_real_time_measurement_id()).orElse(null);

        // then
        assertNotNull(saved);
    }
}