package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Users;
import com.example.cdc_batch_dml.writer.UpdateUsersWriter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class UpdateUsersReader implements ItemReader<Users> {
    private static final Logger log = LoggerFactory.getLogger(UpdateUsersReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 20; // 최대 데이터 개수

    @Override
    public Users read() throws Exception {
        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Users users = null;
        Long randomRoleIdx;
        Map<String, Object> result = getInfo();
        if(result != null) {
            if (result.get("role_idx") != null) {
                Number roleIdxValue = (Number) result.get("role_idx"); // 안전하게 변환
                Long roleIdx = roleIdxValue.longValue();

                if (roleIdx == 1) {
                    randomRoleIdx = ThreadLocalRandom.current().nextBoolean() ? 2L : 3L;
                } else if (roleIdx == 2) {
                    randomRoleIdx = ThreadLocalRandom.current().nextBoolean() ? 1L : 3L;
                } else {
                    randomRoleIdx = ThreadLocalRandom.current().nextBoolean() ? 1L : 2L;
                }
            } else {
                log.error("role_idx is null for result: {}", result);
                return null;
            }

            users = new Users(((Number) result.get("idx")).longValue(), null, randomRoleIdx);
        }
        log.info("IDX: {}, ROLE_IDX: {}", users.getIdx(), users.getRoleIdx());

        currentCount++;

        return users;
    }

    private Map<String, Object> getInfo() {
        String sql = "SELECT idx, role_idx FROM users";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

        if (result.isEmpty()) {
            return null;
        }

        // 랜덤으로 하나 선택
        int randomIndex = ThreadLocalRandom.current().nextInt(result.size());
        return result.get(randomIndex);
    }
}

