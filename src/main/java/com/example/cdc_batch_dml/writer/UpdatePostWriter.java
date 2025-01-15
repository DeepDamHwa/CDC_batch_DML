package com.example.cdc_batch_dml.writer;

import com.example.cdc_batch_dml.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePostWriter implements ItemWriter<Post> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Post> items) throws Exception {
        String sql = "UPDATE POST SET MODIFIED_AT = ? WHERE IDX = ?";

        for (Post post : items) {
            jdbcTemplate.update(sql, post.getModifiedAt(), post.getIdx());
        }

    }
}
