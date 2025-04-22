package io.protopanda.config;

import io.protopanda.listener.JobCompletionNotificationListener;
import io.protopanda.listener.LogStepExecutionListener;
import io.protopanda.model.DataIngestionPayload;
import io.protopanda.reader.CustomReader;
import io.protopanda.writer.CustomWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final LogStepExecutionListener logStepExecutionListener;

    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Value("${batch.partition-count:10}")
    private int partitionCount;

    @Bean
    @StepScope
    public CustomReader customReader(
            @Value("#{stepExecutionContext[startIndex]}") long startIndex,
            @Value("#{stepExecutionContext[endIndex]}") long endIndex) {
        return new CustomReader(startIndex, endIndex);
    }

    @Bean
    public Job partitionedJob(JobRepository jobRepository, Step partitionStep) {
        return new JobBuilder("partitionedJob", jobRepository)
                .start(partitionStep)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .build();
    }

    @Bean
    public Partitioner customPartitioner() {
        return new io.protopanda.partitioner.CustomPartitioner();
    }

    @Bean
    public TaskExecutorPartitionHandler partitionHandler(Step mainStep) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(mainStep);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor());
        handler.setGridSize(partitionCount);
        return handler;
    }

    @Bean
    public Step partitionStep(JobRepository jobRepository, TaskExecutorPartitionHandler partitionHandler) {
        return new StepBuilder("partitionStep", jobRepository)
                .partitioner("mainStep", customPartitioner())
                .partitionHandler(partitionHandler)
                .listener(logStepExecutionListener)
                .build();
    }

    @Bean
    public Step mainStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         ItemReader<DataIngestionPayload> customReader,
                         CustomWriter customWriter) {
        return new StepBuilder("mainStep", jobRepository)
                .<DataIngestionPayload, DataIngestionPayload>chunk(7, transactionManager)
                .reader(customReader)
                .writer(customWriter)
                .listener(logStepExecutionListener)
                .build();
    }
}
