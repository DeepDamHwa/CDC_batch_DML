package com.example.cdc_batch_dml.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
    private Long idx;
    private String name;
    private Long roleIdx;
}