package com.moneybricks.account.service;

import com.moneybricks.account.dto.DepositDTO;

import java.util.List;

public interface DepositHistoryService {
    // 특정 계좌의 모든 입금 내역 조회
    List<DepositDTO> getDepositHistory(String username);

    // 현재 계좌의 입금 내역 조회
    List<DepositDTO> getCurrentDepositHistory(String username);
}
