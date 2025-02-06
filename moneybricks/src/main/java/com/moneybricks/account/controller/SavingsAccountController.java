package com.moneybricks.account.controller;

import com.moneybricks.account.dto.DepositCreateDTO;
import com.moneybricks.account.dto.SavingsAccountDTO;
import com.moneybricks.account.service.SavingsAccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingsAccountController {private final SavingsAccountServiceImpl savingsAccountService;

    // 계좌 조회
    @GetMapping("/account")
    public ResponseEntity<SavingsAccountDTO> getSavingsAccount(Principal principal) {
        String username = principal.getName();

        // 해당 사용자의 계좌 정보 조회
        SavingsAccountDTO savingsAccountDTO = savingsAccountService.getSavingsAccount(username);

        return ResponseEntity.ok(savingsAccountDTO);
    }

    // 만기 여부 확인 및 처리
    @PostMapping("/check-maturity")
    public ResponseEntity<Map<String, Boolean>> checkMaturityAndProcess(Principal principal) {
        String username = principal.getName();

        // 만기 여부 확인 및 처리
        boolean isMatured = savingsAccountService.checkMaturityAndProcess(username); // 만기 처리 여부 반환

        // 만기 처리 결과를 포함하여 응답 반환
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMatured", isMatured); // 만기 여부 포함

        return ResponseEntity.ok(response); // 클라이언트에 만기 여부 전달
    }


    // 입금
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(Principal principal, @RequestBody @Valid DepositCreateDTO depositCreateDTO) {
        String username = principal.getName();

        // 입금 처리
        savingsAccountService.deposit(username, depositCreateDTO);

        return ResponseEntity.ok("입금이 완료되었습니다.");
    }

    // 계좌 갱신
    @PostMapping("/renew")
    public ResponseEntity<String> renewSavingsAccount(Principal principal, @RequestParam("renewalPeriod") int renewalPeriod ) {
        String username = principal.getName();

        // 계좌 갱신 처리
        savingsAccountService.renewSavingsAccount(username, renewalPeriod);

        return ResponseEntity.ok("계좌 갱신이 완료되었습니다.");
    }

    // 중도 해지
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSavingsAccount(Principal principal) {
        String username = principal.getName();

        // 계좌 중도 해지 처리
        savingsAccountService.cancelSavingsAccount(username);

        return ResponseEntity.ok("계좌 중도 해지가 완료되었습니다.");
    }
}