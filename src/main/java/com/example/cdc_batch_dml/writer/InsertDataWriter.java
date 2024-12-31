package com.example.cdc_batch_dml.writer;

import com.example.cdc_batch_dml.entity.Interaction;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class InsertDataWriter {

    @Bean
    public JdbcBatchItemWriter<Interaction> jdbcWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Interaction>()
                .dataSource(dataSource)
                .sql("INSERT INTO interaction (idx, comment_idx, emoji_idx, user_idx) VALUES (NULL, :commentIdx, :emojiIdx, :userIdx)")
                .beanMapped() // Interaction 객체의 필드를 매핑
                .build();
    }
}
