package io.protopanda.config;

import io.protopanda.model.Employee;
import io.protopanda.processor.EmployeeProcessor;
import io.protopanda.reader.CustomReader;
import io.protopanda.repository.EmployeeRepository;
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
import org.springframework.batch.item.ItemWriter;
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

    private final EmployeeRepository employeeRepository;

    @Bean
    public ItemReader<Employee> itemReader() {
        return new CustomReader();
    }

    @Bean
    public ItemProcessor<Employee, Employee> processor() {
        return new EmployeeProcessor();
    }


    @Bean
    public ItemWriter<Employee> writer(EmployeeRepository repository) {
        return items -> {
            repository.saveAll(items); // Batch saves all employees to the repository
            // Optional: print each employee if you want to debug
            items.forEach(item -> System.out.println(">>> Writing: " + item.toString()));
        };
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
                      PlatformTransactionManager transactionManager,
                      ItemWriter<Employee> writer,
                      ItemReader<Employee> reader) {
        return new StepBuilder("step1", jobRepository)
                .<Employee, Employee>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }
}
