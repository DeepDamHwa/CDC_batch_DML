package com.example.cdc_batch_dml.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Interaction {
    private Long idx;
    private Long commentIdx;
    private Long emojiIdx;
    private Long userIdx;
}

