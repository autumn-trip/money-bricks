package com.moneybricks.account.service;

import com.moneybricks.account.domain.AccountHistory;
import com.moneybricks.account.dto.AccountHistoryDTO;
import com.moneybricks.account.repository.AccountHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class AccountHistoryServiceImpl implements AccountHistoryService {
    private final AccountHistoryRepository accountHistoryRepository;

    // 특정 계좌의 모든 히스토리 목록 조회
    @Override
    public List<AccountHistoryDTO> getAccountHistoryList(String username) {
        List<AccountHistory> accountHistoryList = accountHistoryRepository.findBySavingsAccountMemberUsername(username);

        // AccountHistory를 AccountHistoryListDTO로 변환
        return accountHistoryList.stream()
                .map(accountHistory -> AccountHistoryDTO.builder()
                        .historyId(accountHistory.getHistoryId())
                        .savingsAccountId(accountHistory.getSavingsAccount().getId())
                        .accountNumber(accountHistory.getSavingsAccount().getAccountNumber())
                        .previousMaturityPoints(accountHistory.getPreviousMaturityPoints())
                        .previousDepositCount(accountHistory.getPreviousDepositCount())
                        .previousStatus(String.valueOf(accountHistory.getPreviousStatus()))
                        .previousStartDate(accountHistory.getPreviousStartDate())
                        .previousEndDate(accountHistory.getPreviousEndDate())
                        .build())
                .collect(Collectors.toList());
    }

    // 특정 히스토리 상세 조회
    @Override
    public AccountHistoryDTO getAccountHistory(Long historyId) {
        AccountHistory accountHistory = accountHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("해당 이력을 찾을 수 없습니다."));

        return AccountHistoryDTO.builder()
                .historyId(accountHistory.getHistoryId())
                .savingsAccountId(accountHistory.getSavingsAccount().getId())
                .accountNumber(accountHistory.getSavingsAccount().getAccountNumber())
                .previousMaturityPoints(accountHistory.getPreviousMaturityPoints())
                .previousInterestRate(accountHistory.getPreviousInterestRate())
                .previousDepositCount(accountHistory.getPreviousDepositCount())
                .previousStatus(String.valueOf(accountHistory.getPreviousStatus()))
                .previousStartDate(accountHistory.getPreviousStartDate())
                .previousEndDate(accountHistory.getPreviousEndDate())
                .build();
    }
}
