package io.howstheairtoday.airqualitydomainrds.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAirQualityRealTime is a Querydsl query type for AirQualityRealTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAirQualityRealTime extends EntityPathBase<AirQualityRealTime> {

    private static final long serialVersionUID = 1280436148L;

    public static final QAirQualityRealTime airQualityRealTime = new QAirQualityRealTime("airQualityRealTime");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> air_quality_real_time_measurement_id = createNumber("air_quality_real_time_measurement_id", Long.class);

    public final StringPath co_grade = createString("co_grade");

    public final StringPath co_value = createString("co_value");

    public final DateTimePath<java.time.LocalDateTime> data_time = createDateTime("data_time", java.time.LocalDateTime.class);

    public final StringPath khai_grade = createString("khai_grade");

    public final StringPath khai_value = createString("khai_value");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath no2_grade = createString("no2_grade");

    public final StringPath no2_value = createString("no2_value");

    public final StringPath o3_grade = createString("o3_grade");

    public final StringPath o3_value = createString("o3_value");

    public final StringPath pm10_grade = createString("pm10_grade");

    public final StringPath pm10_value = createString("pm10_value");

    public final StringPath pm25_grade = createString("pm25_grade");

    public final StringPath pm25_value = createString("pm25_value");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath sido_name = createString("sido_name");

    public final StringPath so2_grade = createString("so2_grade");

    public final StringPath so2_value = createString("so2_value");

    public final StringPath station_name = createString("station_name");

    public QAirQualityRealTime(String variable) {
        super(AirQualityRealTime.class, forVariable(variable));
    }

    public QAirQualityRealTime(Path<? extends AirQualityRealTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAirQualityRealTime(PathMetadata metadata) {
        super(AirQualityRealTime.class, metadata);
    }

}

