package com.moneybricks.member.controller;

import com.moneybricks.member.dto.*;
import com.moneybricks.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@Valid @RequestBody MemberSignUpDTO memberSignUpDTO, BindingResult bindingResult) {
        memberService.registerMember(memberSignUpDTO);
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시, 에러 메시지를 반환
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    // 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberViewDTO> getMemberInfo(Principal principal) {
        String username = principal.getName();
        MemberViewDTO memberInfo = memberService.getMemberInfo(username);
        return ResponseEntity.ok(memberInfo);
    }

    // 회원 정보 수정
    @PutMapping("/me")
    public ResponseEntity<String> updateMember(
            Principal principal,
            @Valid @RequestBody MemberUpdateDTO updateDTO
    ) {
        String username = principal.getName();  // 로그인한 사용자의 username을 가져옵니다.
        memberService.updateMember(username, updateDTO);  // username을 사용해 업데이트
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    // 비밀번호 변경
    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            Principal principal,
            @Valid @RequestBody PasswordChangeDTO passwordChangeDTO
    ) {
        String username = principal.getName();  // 로그인한 사용자의 username을 가져옵니다.
        memberService.changePassword(username, passwordChangeDTO);  // username을 사용해 비밀번호 변경
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 회원 탈퇴 (논리적 삭제)
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMember(Principal principal) {
        String username = principal.getName();  // 로그인한 사용자의 username을 가져옵니다.
        memberService.deleteMember(username);  // username을 사용해 삭제
        return ResponseEntity.ok("회원이 삭제되었습니다.");
    }

    // 중복 체크 (아이디, 이메일, 닉네임을 한 번에 처리)
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, String>> checkDuplicate(@RequestParam Map<String, String> params) {
        Map<String, String> result = memberService.checkDuplicate(params);
        return ResponseEntity.ok(result);
    }

    // 비밀번호 확인
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequestDTO passwordRequestDTO, Principal principal) {
        boolean isValid = memberService.verifyPassword(principal.getName(), passwordRequestDTO.getPassword());
        if (isValid) {
            return ResponseEntity.ok("비밀번호 인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
    }

    @PutMapping("/updateFirstLoginFlag")
    public ResponseEntity<?> updateFirstLoginFlag(Principal principal) {
        String username = principal.getName();
        memberService.updateFirstLoginFlagToFalse(username);
        return ResponseEntity.ok("로그인 플래그가 업데이트되었습니다.");
    }
}
