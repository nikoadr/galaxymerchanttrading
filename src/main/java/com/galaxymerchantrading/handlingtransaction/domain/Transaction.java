package com.galaxymerchantrading.handlingtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private long id;
    private String content;
    private Integer comodityId;
    private String code;

}
