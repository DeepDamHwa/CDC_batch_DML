package com.example.cdc_batch_dml.job;

import com.example.cdc_batch_dml.entity.Interaction;
import com.example.cdc_batch_dml.entity.Users;
import com.example.cdc_batch_dml.reader.DeleteDataReader;
import com.example.cdc_batch_dml.reader.InsertDataReader;
import com.example.cdc_batch_dml.reader.InsertUsersReader;
import com.example.cdc_batch_dml.reader.UpdateDataReader;
import com.example.cdc_batch_dml.writer.DeleteDataWriter;
import com.example.cdc_batch_dml.writer.InsertDataWriter;
import com.example.cdc_batch_dml.writer.InsertUsersWriter;
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

    // Interaction table
    private final InsertDataReader insertReader;
    private final UpdateDataReader updateReader;
    private final DeleteDataReader deleteReader;

    // Users table
    private final InsertUsersReader insertUsersReader;

    private final ItemProcessor<Interaction, Interaction> interactionProcessor;
    private final ItemProcessor<Users, Users> usersProcessor;

    // Interaction table
    private final InsertDataWriter insertWriter;
    private final UpdateDataWriter updateWriter;
    private final DeleteDataWriter deleteWriter;

    // Users table
    private final InsertUsersWriter insertUsersWriter;


    @Bean
    public Job dataJob() {
        return new JobBuilder("dataJob", jobRepository)
                .start(insertDataStep())
                .next(updateDataStep())
                .next(deleteDataStep())
                .next(insertUserStep())
                .build();
    }

    @Bean
    public Step insertDataStep() {
        return new StepBuilder("insertDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(insertReader)
                .processor(interactionProcessor)
                .writer(insertWriter)
                .build();
    }

    @Bean
    public Step updateDataStep() {
        return new StepBuilder("updateDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(updateReader)
                .processor(interactionProcessor)
                .writer(updateWriter)
                .build();
    }

    @Bean
    public Step deleteDataStep() {
        return new StepBuilder("deleteDataStep", jobRepository)
                .<Interaction, Interaction>chunk(10, transactionManager)
                .reader(deleteReader)
                .processor(interactionProcessor)
                .writer(deleteWriter)
                .build();
    }

    @Bean
    public Step insertUserStep() {
        return new StepBuilder("insertUserStep", jobRepository)
                .<Users, Users>chunk(10, transactionManager)
                .reader(insertUsersReader)
                .processor(usersProcessor)
                .writer(insertUsersWriter)
                .build();
    }
}
