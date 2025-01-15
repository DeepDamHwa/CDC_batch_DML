package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Users;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InsertUsersReader implements ItemReader<Users> {

    private static final Logger log = LoggerFactory.getLogger(InsertUsersReader.class);
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 20; // 최대 데이터 개수
    private Long cnt = null;

    @Override
    public Users read() throws Exception{
        // cnt 값을 한 번만 초기화
        if (cnt == null) {
            cnt = getCnt(); // 데이터베이스에서 현재 users 개수 가져오기
            log.info("Initial count of users (cnt): {}", cnt);
        }

        log.info(">>>>>>>>>>>>>currentCount:{}", currentCount);
        if (currentCount >= maxCount) {
            return null;
        }

        Users users;
        Long randomRoleIdx = (long) (ThreadLocalRandom.current().nextInt(3) + 1);  // 1~3

        String name = "USER_" + Long.toString(cnt+1);
        log.info(">>>>>>>>>>>>>name:{}", name);

        users = new Users(null, name, randomRoleIdx);
        currentCount++;
        cnt++;

        return users;
    }

    private Long getCnt() {
        String sql = "SELECT COUNT(*) FROM users";
        Long cnt = jdbcTemplate.queryForObject(sql, Long.class);

        return cnt;
    }

}
