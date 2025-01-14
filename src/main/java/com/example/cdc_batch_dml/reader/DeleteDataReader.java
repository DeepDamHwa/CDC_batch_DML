package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Interaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DeleteDataReader implements ItemReader<Interaction> {
    private static final Logger log = LoggerFactory.getLogger(DeleteDataReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;
    private final int maxCount = 30;

    @Override
    public Interaction read() throws Exception{
        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Interaction interaction = null;
        try {
            Long idx = getIdx();
            log.info(">>>>>>>>>>>>>deleteIdx:{}", idx);
            interaction = new Interaction(idx, null, null, null);

            currentCount++;

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return interaction;
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
