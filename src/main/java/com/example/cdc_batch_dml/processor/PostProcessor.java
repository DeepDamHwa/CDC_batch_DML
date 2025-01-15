package com.example.cdc_batch_dml.processor;

import com.example.cdc_batch_dml.entity.Post;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PostProcessor implements ItemProcessor<Post, Post> {
    @Override
    public Post process(Post item) throws Exception {
        return item;
    }
}
