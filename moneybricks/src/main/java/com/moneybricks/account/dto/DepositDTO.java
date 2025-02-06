package com.moneybricks.account.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 입금 내역 조회
public class DepositDTO {
    private Long id;                 // 입금 내역 ID
    private Long savingsAccountId;   // 해당 계좌 ID
    private String accountNumber;         // 계좌 번호
    private Integer depositAmount; // 입금 금액
    private LocalDateTime depositDate; // 입금 날짜
    private Integer balanceAfterDeposit; // 입금 후 잔액
    private Integer depositCount;     // 입금 횟수
}
