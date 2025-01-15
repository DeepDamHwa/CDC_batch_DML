package com.example.cdc_batch_dml.processor;


import com.example.cdc_batch_dml.entity.Interaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class InteractionProcessor implements ItemProcessor<Interaction, Interaction> {

    @Override
    public Interaction process(Interaction item) throws Exception {
        // 그대로 전달
        return item;
    }
}
