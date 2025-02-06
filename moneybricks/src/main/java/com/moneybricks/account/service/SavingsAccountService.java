package com.moneybricks.account.service;

import com.moneybricks.account.domain.SavingsAccount;
import com.moneybricks.account.dto.DepositCreateDTO;
import com.moneybricks.account.dto.SavingsAccountDTO;

public interface SavingsAccountService {

    SavingsAccountDTO getSavingsAccount(String username);

    // 입금 기능 (하루 1회 제한)
    void deposit(String username, DepositCreateDTO depositCreateDTO);

    // 로그인 성공 시 호출될 만기 여부 확인 메소드
    boolean checkMaturityAndProcess(String username);

    // 계좌 갱신
    void renewSavingsAccount(String username, int renewalPeriod);

    // 적금 계좌 중도해지
    void cancelSavingsAccount(String username);

}
