package io.howstheairtoday.airqualityappbatch.batch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.howstheairtoday.airqualitydomainrds.repository.AirQualityRealTimeRepository;

@ActiveProfiles("test")
@SpringBootTest
public class BatchTests {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AirQualityRealTimeRepository airQualityRealTimeRepository;


    @Test
    void testBatchJob() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("time", String.valueOf(System.currentTimeMillis()))
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        // verify
        Long count = airQualityRealTimeRepository.count();
        Assertions.assertEquals(10, count);
    }
}
