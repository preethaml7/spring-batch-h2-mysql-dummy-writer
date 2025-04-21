package io.protopanda.config;

import io.protopanda.model.DataIngestionPayload;
import io.protopanda.processor.CustomProcessor;
import io.protopanda.reader.CustomReader;
import io.protopanda.writer.CustomWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SpringBatchConfig {

    @Value("${batch.chunk-size:1000}")
    private Integer chunkSize;

    private final CustomWriter itemWriter;

    @Bean
    public ItemReader<DataIngestionPayload> itemReader() {
        return new CustomReader();
    }

    @Bean
    public ItemProcessor<DataIngestionPayload, DataIngestionPayload> processor() {
        return new CustomProcessor();
    }


    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<DataIngestionPayload, DataIngestionPayload>chunk(chunkSize, transactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(itemWriter)
                .build();
    }
}
