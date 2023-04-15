package io.howstheairtoday.airqualityappbatch.scheduler;

import io.howstheairtoday.airqualityappbatch.config.PMForecastBatchConfiguration;
import io.howstheairtoday.airqualityappbatch.listener.JobCompletionNotificationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Log4j2
public class PMForecastSchedulerConfiguration {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final Job pmForecastJpaJob;
    private final Step pmForecastJpaStep;
    private final PMForecastBatchConfiguration pmForecastBatchConfiguration;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Scheduled(cron = "${app.pmForecast.cron}")
    public void pmForecastBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("pmForecastJobId", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();

            pmForecastBatchConfiguration.pmForecastApiMethod();
            JobExecution jobExecution = jobLauncher.run(pmForecastBatchConfiguration.pmForecastJpaJob(null, jobCompletionNotificationListener, pmForecastJpaStep), jobParameters);

            log.info("JobExecution: " + jobExecution.getStatus());
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
