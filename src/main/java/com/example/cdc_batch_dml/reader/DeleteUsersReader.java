package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Interaction;
import com.example.cdc_batch_dml.entity.Users;
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
public class DeleteUsersReader implements ItemReader <Users> {

    private static final Logger log = LoggerFactory.getLogger(DeleteUsersReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;
    private final int maxCount = 5;

    @Override
    public Users read() throws Exception {
        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Users users = null;
        try {
            Long idx = getIdx();
            log.info(">>>>>>>>>>>>>deleteIdx:{}", idx);
            users = new Users(idx, null, null);

            currentCount++;

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return users;
    }

    private Long getIdx() {
        String sql = "SELECT IDX FROM USERS";
        List<Long> idxList = jdbcTemplate.queryForList(sql, Long.class);

        if (idxList.isEmpty()) {
            return null;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(idxList.size());
        return idxList.get(randomIndex);
    }
}