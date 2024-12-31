package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Interaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InsertDataReader implements ItemReader<Interaction> {
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 100; // 최대 데이터 개수

    @Override
    public Interaction read() throws Exception {
        if (currentCount >= maxCount) {
            // null을 반환해 배치 작업 종료
            return null;
        }

        Interaction interaction;
        // 중복이 아닐 때까지 랜덤 값 생성 반복
        do {
            Long randomCommentIdx = (long) (ThreadLocalRandom.current().nextInt(100) + 1);  // 1~100
            Long randomEmojiIdx = (long) (ThreadLocalRandom.current().nextInt(500) + 1);    // 1~500
            Long randomUserIdx = (long) (ThreadLocalRandom.current().nextInt(10) + 1);      // 1~10

            interaction = new Interaction(null, randomCommentIdx, randomEmojiIdx, randomUserIdx);
        } while (isDuplicate(interaction));

        currentCount++; // 읽은 데이터 개수 증가
        return interaction; // 유효한 데이터 반환
    }

    // 중복 확인 메서드
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
}

