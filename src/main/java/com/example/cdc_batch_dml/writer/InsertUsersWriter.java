package com.example.cdc_batch_dml.writer;

import com.example.cdc_batch_dml.entity.Interaction;
import com.example.cdc_batch_dml.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsertUsersWriter implements ItemWriter<Users> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Users> items) throws Exception {
        String sql = "INSERT INTO users (idx, name, role_idx) VALUES (NULL, ?, ?)";

        for (Users users : items) {
            jdbcTemplate.update(sql,
                    users.getName(),
                    users.getRoleIdx());
        }
    }
}
