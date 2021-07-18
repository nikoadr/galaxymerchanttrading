package com.galaxymerchantrading.handlingtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Long resultSum;
    private String resultName;
    private String resultTransaction;
}
