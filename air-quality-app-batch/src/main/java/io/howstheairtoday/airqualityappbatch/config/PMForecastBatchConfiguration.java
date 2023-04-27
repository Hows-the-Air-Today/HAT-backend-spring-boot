package io.howstheairtoday.airqualityappbatch.config;

import io.howstheairtoday.airqualityappbatch.listener.JobCompletionNotificationListener;
import io.howstheairtoday.airqualityappbatch.service.PMForecastService;
import io.howstheairtoday.airqualityappbatch.util.PMForecastDtoToEntityConverter;
import io.howstheairtoday.airqualityclient.dto.response.PMForecastResponse;
import io.howstheairtoday.airqualitydomainrds.entity.PMForecast;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class PMForecastBatchConfiguration {

    private final PMForecastService pmForecastService;

    private int nextIndex = 0;
    private List<PMForecastResponse> collectData = new ArrayList<>();

    @Bean
    public Job pmForecastJpaJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step pmForecastJpaStep) {

        return new JobBuilder("미세먼지/초미세먼지 주간 예보 데이터 저장 Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(pmForecastJpaStep)
                .end()
                .build();
    }

    @Bean
    public Step pmForecastJpaStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, JpaItemWriter<PMForecast> jpaItemWriter) {

        return new StepBuilder("미세먼지/초미세먼지 주간 예보 데이터 저장 Step", jobRepository)
                .<PMForecast, PMForecast>chunk(20, platformTransactionManager)
                .reader(pmForecastItemCollectReader())
                .writer(jpaItemWriter)
                .build();
    }

    public void pmForecastApiMethod() {

        // 오늘 날짜로 조회
        LocalDate searchDate = LocalDate.now();
        // 조회할 통보 코드
        List<String> informCodeList = List.of("PM10", "PM25");

        try {
            nextIndex = 0;
            collectData = new ArrayList<>();

            for (String informCode : informCodeList) {
                // 각 informCode에 대해 API 호출 후 결과를 collectData에 추가
                // collectData는 List<PMForecastResponse> 타입
                collectData.addAll(pmForecastService.getPMForecastData(searchDate, informCode));
            }
        } catch (Exception exception) {
            log.error("exception", exception);
        }
    }

    @Bean
    public ItemReader<PMForecast> pmForecastItemCollectReader() {

        return new ItemReader<PMForecast>() {
            @Override
            public PMForecast read() {

                PMForecast nextCollect = null;

                if (nextIndex < collectData.size()) {
                    nextCollect = dtoToEntity(nextIndex);
                    nextIndex++;
                }
                return nextCollect;
            }
        };
    }

    @Bean
    public JpaItemWriter<PMForecast> pmForecastJpaItemWriter(EntityManagerFactory entityManagerFactory) {

        return new JpaItemWriterBuilder<PMForecast>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    private PMForecast dtoToEntity(int id) {

        return PMForecastDtoToEntityConverter.toEntityWithId(collectData.get(id), (long) id);
    }
}
