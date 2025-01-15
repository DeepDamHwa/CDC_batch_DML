package com.example.cdc_batch_dml.processor;

import com.example.cdc_batch_dml.entity.Users;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UsersProcessor implements ItemProcessor<Users, Users> {
    @Override
    public Users process(Users item) throws Exception {
        return item;
    }
}
