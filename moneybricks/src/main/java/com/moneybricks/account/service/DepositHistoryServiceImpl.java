package com.moneybricks.account.service;

import com.moneybricks.account.domain.DepositHistory;
import com.moneybricks.account.domain.SavingsAccount;
import com.moneybricks.account.dto.DepositDTO;
import com.moneybricks.account.repository.DepositHistoryRepository;
import com.moneybricks.account.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class DepositHistoryServiceImpl implements DepositHistoryService {

    private final DepositHistoryRepository depositHistoryRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    // 특정 계좌의 모든 입금 내역 조회
    @Override
    public List<DepositDTO> getDepositHistory(String username) {

        List<DepositHistory> depositHistoryList = depositHistoryRepository.findBySavingsAccountMemberUsername(username);

        return depositHistoryList.stream()
                .map(depositHistory -> DepositDTO.builder()
                        .id(depositHistory.getId())
                        .savingsAccountId(depositHistory.getSavingsAccount().getId())
                        .accountNumber(depositHistory.getSavingsAccount().getAccountNumber())
                        .depositAmount(depositHistory.getDepositAmount())
                        .depositDate(depositHistory.getDepositDate())
                        .balanceAfterDeposit(depositHistory.getBalanceAfterDeposit())
                        .build())
                .collect(Collectors.toList());
    }

    // 현재 계좌의 입금 내역 조회
    @Override
    public List<DepositDTO> getCurrentDepositHistory(String username) {

        SavingsAccount currentAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        // 현재 계좌의 startDate 이후로 이루어진 입금 내역만 조회
        LocalDate startDate = currentAccount.getStartDate();

        // 해당 계좌의 입금 내역 조회 (startDate 이후의 입금 내역)
        List<DepositHistory> depositHistoryList =
                depositHistoryRepository.findBySavingsAccountIdAndDepositDateAfter(currentAccount.getId(), startDate.atStartOfDay());

        // 입금 내역 DTO로 변환하여 반환
        return depositHistoryList.stream()
                .map(depositHistory -> DepositDTO.builder()
                        .id(depositHistory.getId())
                        .savingsAccountId(depositHistory.getSavingsAccount().getId())
                        .accountNumber(depositHistory.getSavingsAccount().getAccountNumber()) // 계좌 번호 그대로 사용
                        .depositAmount(depositHistory.getDepositAmount())
                        .depositDate(depositHistory.getDepositDate())
                        .balanceAfterDeposit(depositHistory.getBalanceAfterDeposit())
                        .build())
                .collect(Collectors.toList());
    }
}
