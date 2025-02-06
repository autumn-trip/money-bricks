package com.moneybricks.account.domain;

import com.moneybricks.common.domain.BaseEntity;
import com.moneybricks.member.domain.Member;
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
@Table(name = "savings_accounts", indexes = {
        @Index(name = "idx_member_id", columnList = "member_id"),
        @Index(name = "idx_savings_account_status", columnList = "status")
})
// 적금 테이블
public class SavingsAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 해당 적금을 만든 사용자

    @Column(nullable = false)
    private String accountNumber; // 계좌번호 (고유값)

    @Column(nullable = false)
    private BigDecimal interestRate; // 이자율 (%)

    @Column(nullable = false)
    private Integer depositCount; // 입금 횟수

    @Column(nullable = false)
    private LocalDate startDate; // 시작 날짜

    @Column(nullable = false)
    private LocalDate endDate; // 만기 날짜

    @Column(nullable = false)
    private Integer totalAmount; // 총 납입한 포인트 (이자 제외)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SavingsAccountStatus status; // 적금 상태 (예: 진행중, 완료)

    public void renewAccount(int renewalPeriod) {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(renewalPeriod);
        this.interestRate = new BigDecimal("1.50");
        this.depositCount = 0;
        this.totalAmount = 0;
        this.status = SavingsAccountStatus.ACTIVE;
    }

    public void changeInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void incrementDepositCount() {
        this.depositCount++;
    }

    public void changeTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void changeStatus(SavingsAccountStatus savingsAccountStatus) {
        this.status = savingsAccountStatus;
    }
}
