package io.howstheairtoday.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job myJob;

    @Scheduled(cron = "0 30 * * * *")
    public void runMyJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("jobId", String.valueOf(System.currentTimeMillis()))
            .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);

        System.out.println("JobExecution: " + jobExecution.getStatus());
    }
}