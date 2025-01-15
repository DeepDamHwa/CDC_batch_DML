package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Post;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class UpdatePostReader implements ItemReader<Post> {

    private static final Logger log = LoggerFactory.getLogger(UpdatePostReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 100; // 최대 데이터 개수

    @Override
    public Post read() throws Exception {
        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Post post;
        Long postIdx = getIdx();
        post = new Post(postIdx, null, LocalDateTime.now(), null);

        currentCount++;

        return post;
    }

    private Long getIdx() {
        String sql = "SELECT IDX FROM POST";
        List<Long> idxList = jdbcTemplate.queryForList(sql, Long.class);

        if (idxList.isEmpty()) {
            return null;
        }

        // 랜덤으로 하나 선택
        int randomIndex = ThreadLocalRandom.current().nextInt(idxList.size());
        return idxList.get(randomIndex);
    }
}
