package com.example.cdc_batch_dml.job;

import com.example.cdc_batch_dml.entity.Interaction;
import com.example.cdc_batch_dml.reader.DeleteDataReader;
import com.example.cdc_batch_dml.reader.InsertDataReader;
import com.example.cdc_batch_dml.reader.UpdateDataReader;
import com.example.cdc_batch_dml.writer.DeleteDataWriter;
import com.example.cdc_batch_dml.writer.InsertDataWriter;
import com.example.cdc_batch_dml.writer.UpdateDataWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InsertDataConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final InsertDataReader insertReader;
    private final UpdateDataReader updateReader;
    private final DeleteDataReader deleteReader;

    private final ItemProcessor<Interaction, Interaction> processor;

    private final InsertDataWriter insertWriter;
    private final UpdateDataWriter updateWriter;
    private final DeleteDataWriter deleteWriter;


    @Bean
    public Job dataJob() {
        return new JobBuilder("dataJob", jobRepository)
                .start(insertDataStep())
                .next(updateDataStep())
                .next(deleteDataStep())
                .build();
    }

    @Bean
    public Step insertDataStep() {
        return new StepBuilder("insertDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(insertReader)
                .processor(processor)
                .writer(insertWriter)
                .build();
    }

    @Bean
    public Step updateDataStep() {
        return new StepBuilder("updateDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(updateReader)
                .processor(processor)
                .writer(updateWriter)
                .build();
    }

    @Bean
    public Step deleteDataStep() {
        return new StepBuilder("deleteDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(deleteReader)
                .processor(processor)
                .writer(deleteWriter)
                .build();
    }
}
