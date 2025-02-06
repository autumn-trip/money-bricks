package com.moneybricks.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 계좌 이력 조회
public class AccountHistoryDTO {
    private Long historyId;            // 이력 고유 ID
    private Long savingsAccountId;     // 해당 계좌 ID
    private String accountNumber;          // 계좌 번호
    private Integer previousMaturityPoints;    // 이전 만기 포인트
    private BigDecimal previousInterestRate; // 이전 이자율
    private Integer previousDepositCount; // 이전 입금 횟수
    private String previousStatus; // 이전 계좌 상태
    private LocalDate previousStartDate; // 이전 시작 날짜
    private LocalDate previousEndDate;   // 이전 만기 날짜
}
