package com.example.cdc_batch_dml;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchJobRunner implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        System.out.println("배치 작업 시작");

        try {
            Job job = (Job) applicationContext.getBean("insertDataJob");
            System.out.println("배치 작업: " + job.getName());

            // JobParameters 생성
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // Job 실행
            jobLauncher.run(job, jobParameters);
            System.out.println("배치 작업 성공");
        } catch (Exception e) {
            System.out.println("배치 작업 실패");
            e.printStackTrace();
        } finally {
            // 어플리케이션 종료
            System.exit(0);
        }
    }
}
