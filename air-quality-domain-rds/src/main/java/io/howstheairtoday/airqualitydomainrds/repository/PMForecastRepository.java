package io.howstheairtoday.airqualitydomainrds.repository;

import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PMForecastRepository extends JpaRepository<PMForecast, Long> {

    List<PMForecast> findAll();

    List<PMForecast> findByInformCode(String informCode);
}
