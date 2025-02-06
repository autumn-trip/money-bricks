package com.moneybricks.account.controller;

import com.moneybricks.account.dto.DepositDTO;
import com.moneybricks.account.service.DepositHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositHistoryController {

    private final DepositHistoryServiceImpl depositHistoryService;

    // 특정 username의 모든 입금 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<DepositDTO>> getDepositHistory(Principal principal) {
        String username = principal.getName();
        List<DepositDTO> depositHistory = depositHistoryService.getDepositHistory(username);
        return ResponseEntity.ok(depositHistory);
    }

    // 특정 username의 입금 내역 (startDate 이후) 조회
    @GetMapping("/current-history")
    public ResponseEntity<List<DepositDTO>> getCurrentDepositHistory(Principal principal) {
        String username = principal.getName();
        List<DepositDTO> currentDepositHistory = depositHistoryService.getCurrentDepositHistory(username);
        return ResponseEntity.ok(currentDepositHistory);
    }
}
