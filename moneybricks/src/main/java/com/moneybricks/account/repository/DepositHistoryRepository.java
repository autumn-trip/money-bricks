package com.moneybricks.account.repository;

import com.moneybricks.account.domain.DepositHistory;
import com.moneybricks.account.domain.SavingsAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long> {
    boolean existsBySavingsAccountAndDepositDateBetween(SavingsAccount savingsAccount, LocalDateTime todayStart, LocalDateTime todayEnd);

    // username을 기준으로 입금 내역 조회 (엔티티 그래프 사용)
    @EntityGraph(attributePaths = {"savingsAccount", "savingsAccount.member"})
    List<DepositHistory> findBySavingsAccountMemberUsername(String username);

    List<DepositHistory> findBySavingsAccountIdAndDepositDateAfter(Long savingsAccountId, LocalDateTime depositDateAfter);
}
