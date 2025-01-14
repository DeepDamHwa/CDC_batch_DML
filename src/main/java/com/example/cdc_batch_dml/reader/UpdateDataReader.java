package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Interaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UpdateDataReader implements ItemReader<Interaction> {
    private static final Logger log = LoggerFactory.getLogger(UpdateDataReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 100; // 최대 데이터 개수

    @Override
    public Interaction read() throws Exception{
        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Interaction interaction = null;
        Long randomCommentIdx, randomEmojiIdx, randomUserIdx;
        do {
            randomCommentIdx = (long) (ThreadLocalRandom.current().nextInt(100) + 1);  // 1~100
            randomEmojiIdx = (long) (ThreadLocalRandom.current().nextInt(500) + 1);    // 1~500
            randomUserIdx = (long) (ThreadLocalRandom.current().nextInt(10) + 1);      // 1~10

            interaction = new Interaction(null, randomCommentIdx, randomEmojiIdx, randomUserIdx);

        }while(!isDuplicate(interaction));

        Long idx = getIdx();
        interaction = new Interaction(idx, randomCommentIdx, randomEmojiIdx, randomUserIdx);
        log.info("IDX: {}, RandomCommentIdx: {}, RandomEmojiIdx: {}, RandomUserIdx: {}", idx, randomCommentIdx, randomEmojiIdx, randomUserIdx);
        currentCount++;
        
        return interaction;
    }

    private boolean isDuplicate(Interaction interaction) {
        String sql = "SELECT COUNT(*) FROM interaction WHERE comment_idx = ? AND emoji_idx = ? AND user_idx = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                interaction.getCommentIdx(),
                interaction.getEmojiIdx(),
                interaction.getUserIdx()
        );
        return count != null && count > 0;
    }

    private Long getIdx() {
        String sql = "SELECT idx FROM interaction";
        List<Long> idxList = jdbcTemplate.queryForList(sql, Long.class);

        if (idxList.isEmpty()) {
            return null;
        }

        // 랜덤으로 하나 선택
        int randomIndex = ThreadLocalRandom.current().nextInt(idxList.size());
        return idxList.get(randomIndex);
    }
}
