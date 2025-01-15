package com.example.cdc_batch_dml.reader;

import com.example.cdc_batch_dml.entity.Users;
import lombok.RequiredArgsConstructor;
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
    private final JdbcTemplate jdbcTemplate;
    private int currentCount = 0;  // 현재 읽은 데이터 개수
    private final int maxCount = 20; // 최대 데이터 개수
    private Long cnt = getCnt();

    @Override
    public Users read() throws Exception{
        if (currentCount >= maxCount) {
            return null;
        }

        Users users;
        Long randomRoleIdx = (long) (ThreadLocalRandom.current().nextInt(3) + 1);  // 1~3

        String name = "USER_" + Long.toString(cnt+1);

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
