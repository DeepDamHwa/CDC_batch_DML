package com.example.cdc_batch_dml.writer;

import com.example.cdc_batch_dml.entity.Interaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UpdateDataWriter implements ItemWriter<Interaction> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Interaction> items) throws Exception {
        String sql = "UPDATE INTERACTION SET COMMENT_IDX = ?, EMOJI_IDX = ?, USER_IDX = ? WHERE IDX = ?";

        for (Interaction interaction : items) {
            jdbcTemplate.update(sql,
                    interaction.getCommentIdx(),
                    interaction.getEmojiIdx(),
                    interaction.getUserIdx(),
                    interaction.getIdx());
        }
    }
}
