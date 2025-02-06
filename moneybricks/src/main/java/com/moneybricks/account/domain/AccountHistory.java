package com.moneybricks.account.domain;

import com.moneybricks.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_histories", indexes = {
        @Index(name = "idx_savings_account_id", columnList = "savings_account_id")
})
public class AccountHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;  // 이력 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount; // 해당 계좌

    @Column
    private Integer previousMaturityPoints; // 만기 포인트 (원금 + 이자)

    @Column(nullable = false)
    private BigDecimal previousInterestRate; // 이전 이자율 (%)

    @Column(nullable = false)
    private Integer previousDepositCount; // 이전 입금 횟수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SavingsAccountStatus previousStatus; // 이전 계좌 상태 (COMPLETED, CANCELED)

    @Column(nullable = false)
    private LocalDate previousStartDate; // 이전 시작 날짜

    @Column(nullable = false)
    private LocalDate previousEndDate; // 이전 만기 날짜

}

