package io.howstheairtoday.batch.scheduler;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.howstheairtoday.batch.configuration.BatchConfiguration;
import io.howstheairtoday.batch.listener.JobCompletionNotificationListener;
import lombok.extern.log4j.Log4j2;

// 잡을 실행시켜줄 스케쥴러
@Component
@EnableScheduling
@Log4j2
public class SchedulerConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BatchConfiguration batchConfiguration;

    @Autowired
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    @Autowired
    private Step jpaStep;


    @Scheduled(cron = "0 30 * * * *")
    public void runMyJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobId", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

            batchConfiguration.method();
            JobExecution jobExecution = jobLauncher.run(batchConfiguration.jpaJob(null, jobCompletionNotificationListener, jpaStep), jobParameters);

            log.info("JobExecution: " + jobExecution.getStatus());
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
}