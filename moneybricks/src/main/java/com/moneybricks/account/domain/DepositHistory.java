package com.moneybricks.account.domain;

import com.moneybricks.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deposit_history", indexes = {
        @Index(name = "idx_savings_account_id", columnList = "savings_account_id")
})
// 입금 내역
public class DepositHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount; // 해당 입금이 일어난 적금 계좌

    @Column(nullable = false)
    private Integer depositAmount; // 입금 금액

    @Column(nullable = false)
    private LocalDateTime depositDate; // 입금 날짜 및 시간

    @Column(nullable = false)
    private Integer balanceAfterDeposit; // 입금 후 잔액 (이자 제외)
}
