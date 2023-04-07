package io.howstheairtoday.airqualitydomainrds;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {AirQualityRealTimeRepository.class})
@EnableJpaRepositories(basePackages = "io.howstheairtoday.airqualitydomainrds.repository")
@EntityScan("io.howstheairtoday.airqualitydomainrds.entity")
// 데이터베이스에 기록 남기기
// @Transactional(propagation = Propagation.NOT_SUPPORTED)
public class RepositoryTests {

    @Autowired
    private AirQualityRealTimeRepository airQualityRealTimeRepository;

    @Test
    public void saveTest() {

        //Given
        // 날짜 타입 변환을 위한 formatter 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2022-03-24 14:00", formatter);

        // AirQualityRealTime 객체 생성 및 초기화하며 각 필드를 초기화
        AirQualityRealTime airQualityRealTime = AirQualityRealTime.builder()
            .airQualityRealTimeMeasurementId(1L)
            .sidoName("서울")
            .stationName("종로구")
            .so2Value("0.003")
            .coValue("0.4")
            .o3Value("0.027")
            .no2Value("0.014")
            .pm10Value("37")
            .pm25Value("21")
            .khaiValue(64)
            .khaiGrade("2")
            .so2Grade("1")
            .coGrade("2")
            .o3Grade("2")
            .no2Grade("1")
            .pm10Grade("1")
            .pm25Grade("2")
            .dataTime(dateTime)
            .build();

        // When
        // 데이터베이스에 저장
        airQualityRealTimeRepository.save(airQualityRealTime);

        // 데이터베이스에서 입력한 값이 입력되었는지 조회
        AirQualityRealTime saved = airQualityRealTimeRepository.findById(
            airQualityRealTime.getAirQualityRealTimeMeasurementId()).orElse(null);

        // then
        assertEquals(saved.getAirQualityRealTimeMeasurementId(), airQualityRealTime.getAirQualityRealTimeMeasurementId());
        assertEquals(saved.getSidoName(), airQualityRealTime.getSidoName());
        assertEquals(saved.getStationName(), airQualityRealTime.getStationName());
        assertEquals(saved.getSo2Value(), airQualityRealTime.getSo2Value());
        assertEquals(saved.getCoValue(), airQualityRealTime.getCoValue());
        assertEquals(saved.getO3Value(), airQualityRealTime.getO3Value());
        assertEquals(saved.getNo2Value(), airQualityRealTime.getNo2Value());
        assertEquals(saved.getPm10Value(), airQualityRealTime.getPm10Value());
        assertEquals(saved.getPm25Value(), airQualityRealTime.getPm25Value());
        assertEquals(saved.getKhaiValue(), airQualityRealTime.getKhaiValue());
        assertEquals(saved.getKhaiGrade(), airQualityRealTime.getKhaiGrade());
        assertEquals(saved.getSo2Grade(), airQualityRealTime.getSo2Grade());
        assertEquals(saved.getCoGrade(), airQualityRealTime.getCoGrade());
        assertEquals(saved.getO3Grade(), airQualityRealTime.getO3Grade());
        assertEquals(saved.getNo2Grade(), airQualityRealTime.getNo2Grade());
        assertEquals(saved.getPm10Grade(), airQualityRealTime.getPm10Grade());
        assertEquals(saved.getPm25Grade(), airQualityRealTime.getPm25Grade());
        assertEquals(saved.getDataTime(), airQualityRealTime.getDataTime());
    }

}