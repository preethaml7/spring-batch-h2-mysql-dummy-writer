package io.protopanda.listener;

import io.protopanda.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final EmployeeRepository employeeRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("==================================================");
        log.info("🚀 JOB STARTING: {}", jobExecution.getJobInstance().getJobName());
        log.info("🕒 Start Time: {}", jobExecution.getStartTime());
        log.info("==================================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        Duration duration = Duration.between(
                Objects.requireNonNull(jobExecution.getStartTime()),
                jobExecution.getEndTime()
        );

        log.info("==================================================");
        log.info("📋 JOB SUMMARY: {}", jobExecution.getJobInstance().getJobName());
        log.info("🕒 Duration: {} seconds", duration.getSeconds());
        log.info("📌 Final Status: {}", status);

        if (status == BatchStatus.COMPLETED) {
            log.info("✅ JOB COMPLETED SUCCESSFULLY");

            try {
                long employeeCount = employeeRepository.count();
                log.info("👥 Total Employees Processed: {}", employeeCount);
            } catch (Exception e) {
                log.error("❌ Error retrieving employee count: {}", e.getMessage(), e);
            }

        } else if (status == BatchStatus.FAILED) {
            log.error("❌ JOB FAILED");
            jobExecution.getAllFailureExceptions().forEach(ex ->
                    log.error("💥 Exception: {}", ex.getMessage(), ex)
            );
        }

        log.info("==================================================");
    }
}