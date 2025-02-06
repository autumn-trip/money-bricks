package com.moneybricks.account.service;

import com.moneybricks.account.dto.AccountHistoryDTO;

import java.util.List;

public interface AccountHistoryService {

    // 특정 계좌의 모든 히스토리 목록 조회
    List<AccountHistoryDTO> getAccountHistoryList(String username);

    // 특정 히스토리 상세 조회
    AccountHistoryDTO getAccountHistory(Long historyId);
}
