package com.moneybricks.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 계좌 조회
public class SavingsAccountDTO {
    private Long id;                  // 계좌 ID
    private Long memberId;            // 회원 ID
    private String accountNumber;     // 계좌 번호
    private BigDecimal interestRate;  // 이자율
    private LocalDate startDate;      // 시작 날짜
    private LocalDate endDate;        // 만기 날짜
    private Integer totalAmount;      // 총 금액
    private String status;            // 계좌 상태 (ACTIVE, COMPLETED, CANCELED)
    private Integer depositCount; // 입금 횟수
}
