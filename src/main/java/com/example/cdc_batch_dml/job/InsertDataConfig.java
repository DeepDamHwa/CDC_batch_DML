package com.example.cdc_batch_dml.job;

import com.example.cdc_batch_dml.entity.Interaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InsertDataConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<Interaction> reader;
    private final ItemProcessor<Interaction, Interaction> processor;
    private final ItemWriter<Interaction> writer;

    @Bean
    public Job insertDataJob() {
        return new JobBuilder("insertDataJob", jobRepository)
                .start(insertDataStep())
                .build();
    }

    @Bean
    public Step insertDataStep() {
        return new StepBuilder("insertDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
