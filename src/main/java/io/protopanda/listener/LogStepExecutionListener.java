package io.protopanda.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
public class LogStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("--------------------------------------------------");
        log.info("🟡 STEP STARTING: {}", stepExecution.getStepName());
        log.info("🕒 Start Time: {}", stepExecution.getStartTime());
        log.info("--------------------------------------------------");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        Duration duration = Duration.between(
                Objects.requireNonNull(stepExecution.getStartTime()),
                stepExecution.getEndTime()
        );

        log.info("--------------------------------------------------");
        log.info("✅ STEP COMPLETED: {}", stepExecution.getStepName());
        log.info("🕒 Duration       : {} seconds", duration.getSeconds());
        log.info("📊 Summary        : Read = {}, Written = {}, Skipped = {}",
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getSkipCount());

        if (!stepExecution.getFailureExceptions().isEmpty()) {
            log.error("❌ STEP ERRORS:");
            stepExecution.getFailureExceptions().forEach(ex ->
                    log.error("   💥 {}", ex.getMessage(), ex)
            );
        } else {
            log.info("✅ Step executed without errors.");
        }

        log.info("📌 Exit Status    : {}", stepExecution.getExitStatus());
        log.info("--------------------------------------------------");

        return stepExecution.getExitStatus();
    }
}
