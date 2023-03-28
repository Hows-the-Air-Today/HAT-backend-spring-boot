package io.howstheairtoday.airqualitydomainrds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;

public interface AirQualityRealTimeRepository extends JpaRepository<AirQualityRealTime, Long> {
}
