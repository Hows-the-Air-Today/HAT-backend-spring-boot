package io.howstheairtoday.airqualityappbatch.config;

import io.howstheairtoday.airqualityappbatch.listener.JobCompletionNotificationListener;
import io.howstheairtoday.airqualityappbatch.service.AirQualityRealTimeService;
import io.howstheairtoday.airqualitydomainrds.dto.response.CurrentDustResponseDTO;
import io.howstheairtoday.airqualitydomainrds.entity.AirQualityRealTime;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

// 배치 Job 생성을 위한 Configuration
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private List<CurrentDustResponseDTO> collectData = new ArrayList<>(); //Rest로 가져온 데이터를 리스트에 넣는다.
    private int nextIndex = 0;//리스트의 데이터를 하나씩 인덱스를 통해 가져온다.

    @Autowired
    private AirQualityRealTimeService airQualityRealTimeService;

    @Autowired
    // Open API를 호출
    public void method() {
        try {
            nextIndex = 0;
            collectData = airQualityRealTimeService.getAirQualityData();
        } catch (Exception e) {
        }
    }

    // DTO를 Entity로 변환하는 메서드
    public AirQualityRealTime dtoToEntity(int i) {

        return AirQualityRealTime.builder()
                .airQualityRealTimeMeasurementId((long) i)
                .dataTime(collectData.get(i).getDataTime())
                .coGrade(collectData.get(i).getCoGrade())
                .coValue(collectData.get(i).getCoValue())
                .khaiGrade(collectData.get(i).getKhaiGrade())
                .khaiValue(collectData.get(i).getKhaiValue())
                .no2Grade(collectData.get(i).getNo2Grade())
                .no2Value(collectData.get(i).getNo2Value())
                .o3Grade(collectData.get(i).getO3Grade())
                .o3Value(collectData.get(i).getO3Value())
                .so2Grade(collectData.get(i).getSo2Grade())
                .so2Value(collectData.get(i).getSo2Value())
                .pm10Grade(collectData.get(i).getPm10Grade())
                .pm10Value(collectData.get(i).getPm10Value())
                .pm25Grade(collectData.get(i).getPm25Grade())
                .pm25Value(collectData.get(i).getPm25Value())
                .sidoName(collectData.get(i).getSidoName())
                .stationName(collectData.get(i).getStationName())
                .build();
    }

    //Rest API로 데이터를 가져온다.
    @Bean
    public ItemReader<AirQualityRealTime> restItCollectReader() {

        return new ItemReader<AirQualityRealTime>() {

            @Override
            public AirQualityRealTime read() {

                //ItemReader는 반복문으로 동작한다. 하나씩 Writer로 전달해야 한다.
                AirQualityRealTime nextCollect = null;

                if (nextIndex < collectData.size()) {
                    //전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달
                    nextCollect = dtoToEntity(nextIndex);
                    nextIndex++;
                }
                return nextCollect;
            }
        };
    }

    // 데이터 삽입
    @Bean
    public JpaItemWriter<AirQualityRealTime> jpaItemWriter(EntityManagerFactory entityManagerFactory) {

        return new JpaItemWriterBuilder<AirQualityRealTime>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


    // 하나의 스텝 구성
    @Bean
    public Step jpaStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaItemWriter<AirQualityRealTime> writer) {
        return new StepBuilder("jpaStep", jobRepository)
                // 청크의 크기를 너무 크게하면 한 번에 많은 양의 데이터를 처리할 수 있어 효율적이지만 청크 하나가 너무 커지면
                // 메모리 사용량이 증가하고 오히려 처리 시간이 길어질 수 있습니다.
                // 일반적으로 100에서 1000 사이로 지정하는게 좋다고 합니다.
                .<AirQualityRealTime, AirQualityRealTime>chunk(642, transactionManager)
                .reader(restItCollectReader())
                .writer(writer)
                .build();
    }

    // 스텝들을 가지고 잡을 구성
    @Bean
    public Job jpaJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step jpaStep) {
        method();
        return new JobBuilder("jpaJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(jpaStep)
                .end()
                .build();
    }
}