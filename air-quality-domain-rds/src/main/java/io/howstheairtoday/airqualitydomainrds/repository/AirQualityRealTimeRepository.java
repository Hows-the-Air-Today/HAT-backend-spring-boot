package io.howstheairtoday.airqualitydomainrds.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;

public interface AirQualityRealTimeRepository extends JpaRepository<AirQualityRealTime, Long> {

    // @Query("select a from AirQualityRealTime a where a.stationName = :stationName")
    AirQualityRealTime findAirQualityRealTimeByStationName(String stationName);

    @Query("select a from AirQualityRealTime a where a.khaiValue > -1 order by a.airQualityRealTimeMeasurementId asc")
    List<AirQualityRealTime> findBest10(Pageable pageable);

    @Query("select a from AirQualityRealTime a where a.khaiValue > -1 order by a.airQualityRealTimeMeasurementId desc")
    List<AirQualityRealTime> findWorst10(Pageable pageable);
}
