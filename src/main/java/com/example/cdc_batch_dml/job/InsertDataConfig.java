package com.example.cdc_batch_dml.job;

import com.example.cdc_batch_dml.entity.Interaction;
import com.example.cdc_batch_dml.entity.Post;
import com.example.cdc_batch_dml.entity.Users;
import com.example.cdc_batch_dml.reader.*;
import com.example.cdc_batch_dml.writer.*;
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

    private final ItemProcessor<Interaction, Interaction> interactionProcessor;

    private final InsertDataWriter insertWriter;
    private final UpdateDataWriter updateWriter;
    private final DeleteDataWriter deleteWriter;

    // Users table
    private final InsertUsersReader insertUsersReader;
    private final UpdateUsersReader updateUsersReader;
    private final DeleteUsersReader deleteUsersReader;

    private final ItemProcessor<Users, Users> usersProcessor;

    private final InsertUsersWriter insertUsersWriter;
    private final UpdateUsersWriter updateUsersWriter;
//    private final DeleteUsersWriter deleteUsersWriter;

    // Post table
    private final InsertPostReader insertPostReader;
    private final UpdatePostReader updatePostReader;
    private final DeletePostReader deletePostReader;

    private final ItemProcessor<Post, Post> postProcessor;

    private final InsertPostWriter insertPostWriter;
    private final UpdatePostWriter updatePostWriter;
    private final DeletePostWriter deletePostWriter;


    @Bean
    public Job dataJob() {
        return new JobBuilder("dataJob", jobRepository)
                .start(insertDataStep())
                .next(updateDataStep())
                .next(deleteDataStep())
                .next(insertUserStep())
                .next(updateUserStep())
//                .next(deleteUserStep())
                .next(insertPostStep())
                .next(updatePostStep())
                .next(deletePostStep())
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

    @Bean
    public Step updateUserStep() {
        return new StepBuilder("updateUserStep", jobRepository)
                .<Users, Users>chunk(10, transactionManager)
                .reader(updateUsersReader)
                .processor(usersProcessor)
                .writer(updateUsersWriter)
                .build();
    }

//    @Bean
//    public Step deleteUserStep() {
//        return new StepBuilder("deleteUserStep", jobRepository)
//                .<Users, Users>chunk(10, transactionManager)
//                .reader(deleteUsersReader)
//                .processor(usersProcessor)
//                .writer(deleteUsersWriter)
//                .build();
//    }

    @Bean
    public Step insertPostStep() {
        return new StepBuilder("insertPostStep", jobRepository)
                .<Post, Post>chunk(10, transactionManager)
                .reader(insertPostReader)
                .processor(postProcessor)
                .writer(insertPostWriter)
                .build();
    }

    @Bean
    public Step updatePostStep() {
        return new StepBuilder("updatePostStep", jobRepository)
                .<Post, Post>chunk(10, transactionManager)
                .reader(updatePostReader)
                .processor(postProcessor)
                .writer(updatePostWriter)
                .build();
    }

    @Bean
    public Step deletePostStep() {
        return new StepBuilder("deletePostStep", jobRepository)
                .<Post, Post>chunk(10, transactionManager)
                .reader(deletePostReader)
                .processor(postProcessor)
                .writer(deletePostWriter)
                .build();
    }
}
