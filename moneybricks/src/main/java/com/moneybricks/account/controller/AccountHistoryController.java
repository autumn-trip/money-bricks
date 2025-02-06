package com.moneybricks.account.controller;

import com.moneybricks.account.dto.AccountHistoryDTO;
import com.moneybricks.account.service.AccountHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account/history")
@RequiredArgsConstructor
public class AccountHistoryController {

    private final AccountHistoryServiceImpl accountHistoryService;

    // 특정 계좌의 모든 히스토리 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<AccountHistoryDTO>> getAccountHistoryList(Principal principal) {
        String username = principal.getName();
        List<AccountHistoryDTO> accountHistoryList = accountHistoryService.getAccountHistoryList(username);
        return ResponseEntity.ok(accountHistoryList);
    }

    // 특정 계좌 이력 상세 조회
    @GetMapping("/{historyId}")
    public ResponseEntity<AccountHistoryDTO> getAccountHistory(@PathVariable Long historyId) {
        AccountHistoryDTO accountHistory = accountHistoryService.getAccountHistory(historyId);
        return ResponseEntity.ok(accountHistory);
    }
}

