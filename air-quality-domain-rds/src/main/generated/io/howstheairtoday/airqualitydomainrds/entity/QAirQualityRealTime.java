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

    public final NumberPath<Long> airQualityRealTimeMeasurementId = createNumber("airQualityRealTimeMeasurementId", Long.class);

    public final StringPath coGrade = createString("coGrade");

    public final StringPath coValue = createString("coValue");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> dataTime = createDateTime("dataTime", java.time.LocalDateTime.class);

    public final StringPath khaiGrade = createString("khaiGrade");

    public final StringPath khaiValue = createString("khaiValue");

    public final StringPath no2Grade = createString("no2Grade");

    public final StringPath no2Value = createString("no2Value");

    public final StringPath o3Grade = createString("o3Grade");

    public final StringPath o3Value = createString("o3Value");

    public final StringPath pm10Grade = createString("pm10Grade");

    public final StringPath pm10Value = createString("pm10Value");

    public final StringPath pm25Grade = createString("pm25Grade");

    public final StringPath pm25Value = createString("pm25Value");

    public final StringPath sidoName = createString("sidoName");

    public final StringPath so2Grade = createString("so2Grade");

    public final StringPath so2Value = createString("so2Value");

    public final StringPath stationName = createString("stationName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

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

