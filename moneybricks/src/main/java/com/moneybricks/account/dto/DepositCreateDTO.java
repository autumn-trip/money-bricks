package com.moneybricks.account.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 입금
public class DepositCreateDTO {
    private Integer depositAmount;      // 입금 금액
}
