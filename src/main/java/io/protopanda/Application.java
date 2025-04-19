package io.protopanda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class Application implements ApplicationRunner {

    private final JobLauncher jobLauncher;

    private final Job importUserJob;

    public static void main(String[] args) {

        SpringApplication.exit(SpringApplication.run(Application.class, args));
    }

    @Override
    public void run(ApplicationArguments args) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // ensures uniqueness
                .toJobParameters();

        log.info("==========================================");
        log.info("🚀 Starting the batch job: {}", importUserJob.getName());
        log.info("📅 Start time: {}", Instant.now());
        log.info("==========================================");

        Instant start = Instant.now();

        try {
            JobExecution jobExecution = jobLauncher.run(importUserJob, jobParameters);

            log.info("🔄 Job Execution Status: {}", jobExecution.getStatus());

            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                log.info("✅ Job completed successfully.");
            } else {
                log.warn("⚠️ Job did not complete successfully.");
                jobExecution.getAllFailureExceptions().forEach(e ->
                        log.error("❌ Exception during job execution: {}", e.getMessage(), e)
                );
            }

        } catch (Exception e) {
            log.error("❌ Failed to execute job: {}", e.getMessage(), e);
        }

        Duration duration = Duration.between(start, Instant.now());
        log.info("⏱️ Job duration: {} seconds", duration.getSeconds());
        log.info("==========================================");
    }
}