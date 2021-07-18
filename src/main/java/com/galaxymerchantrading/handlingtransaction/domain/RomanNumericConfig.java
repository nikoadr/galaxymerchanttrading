package com.galaxymerchantrading.handlingtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RomanNumericConfig {
    private Integer id;
    private String numName;
    private String numCode;
    private Integer numValue;

}
