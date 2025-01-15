package com.example.cdc_batch_dml.writer;

import com.example.cdc_batch_dml.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UpdateUsersWriter implements ItemWriter<Users> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Users> items) throws Exception {
        String sql = "UPDATE USERS SET ROLE_IDX = ? WHERE IDX = ?";

        for (Users users: items) {
            jdbcTemplate.update(sql,
                    users.getRoleIdx(),
                    users.getIdx());
        }
    }
}
