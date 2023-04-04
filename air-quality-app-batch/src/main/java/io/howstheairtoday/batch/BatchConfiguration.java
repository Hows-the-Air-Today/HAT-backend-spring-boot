package io.howstheairtoday.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 배치 Job 생성을 위한 Configuration
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    // JobBuilderFactory와 StepBuilderFactory를 이용해 Job과 Step을 생성.
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep() {
        return stepBuilderFactory.get("myStep")
            .tasklet((stepContribution, chunkContext) -> {
                // 호출, 가공, 삽입 메서드 실행 코드 작성
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Job myJob() {
        return jobBuilderFactory.get("myJob")
            .incrementer(new RunIdIncrementer())
            .start(myStep())
            .build();
    }
}