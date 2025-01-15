package com.example.cdc_batch_dml.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    private Long idx;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long userIdx;
}
