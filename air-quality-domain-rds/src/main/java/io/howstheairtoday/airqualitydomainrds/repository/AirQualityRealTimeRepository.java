package io.howstheairtoday.airqualitydomainrds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;

public interface AirQualityRealTimeRepository extends JpaRepository<AirQualityRealTime, Long> {

    // @Query("select a from AirQualityRealTime a where a.stationName = :stationName")
    List<AirQualityRealTime> findAirQualityRealTimeByStationName(String stationName);
}
