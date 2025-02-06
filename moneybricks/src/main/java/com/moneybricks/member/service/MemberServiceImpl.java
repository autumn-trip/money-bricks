package com.moneybricks.member.service;

import com.moneybricks.account.domain.SavingsAccount;
import com.moneybricks.account.domain.SavingsAccountStatus;
import com.moneybricks.account.repository.SavingsAccountRepository;
import com.moneybricks.member.domain.Member;
import com.moneybricks.member.domain.MemberRole;
import com.moneybricks.member.dto.MemberViewDTO;
import com.moneybricks.member.dto.MemberSignUpDTO;
import com.moneybricks.member.dto.MemberUpdateDTO;
import com.moneybricks.member.dto.PasswordChangeDTO;
import com.moneybricks.member.repository.MemberRepository;
import com.moneybricks.point.domain.Points;
import com.moneybricks.point.domain.PointsActionType;
import com.moneybricks.point.domain.PointsHistory;
import com.moneybricks.point.repository.PointsHistoryRepository;
import com.moneybricks.point.repository.PointsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final PointsRepository pointsRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerMember(MemberSignUpDTO memberSignUpDTO) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberSignUpDTO.getPassword());

        // Member 엔티티 생성
        Member member = Member.builder()
                .username(memberSignUpDTO.getUsername())
                .password(encodedPassword)
                .email(memberSignUpDTO.getEmail())
                .name(memberSignUpDTO.getName())
                .nickname(memberSignUpDTO.getNickname())
                .phoneNumber(memberSignUpDTO.getPhoneNumber())
                .ssn(memberSignUpDTO.getSsn())
                .emailAgreed(memberSignUpDTO.isEmailAgreed())
                .socialLinked(false)
                .deleted(false) // 기본값은 false로 설정
                .firstLoginFlag(true) // 첫 로그인 true
                .build();

        member.addRole(MemberRole.USER); //기본권한 USER추가


        log.info("Saving Member: " + member);
        memberRepository.save(member);
        log.info("Saved Member Successfully"); // 저장 후 로그 확인

        // 포인트 생성 (초기 포인트 10,000 제공)
        Points points = Points.builder()
                .member(member)
                .totalPoints(10000)  // 초기 포인트 10,000
                .availablePoints(10000)  // 즉시 사용 불가
                .savingsUsedPoints(0)
                .lockedFlag(true) // 초기 포인트는 포인트 샵에서 바로 사용 불가능
                .build();

        pointsRepository.save(points);

        // 포인트 히스토리 생성 (회원가입 보너스)
        PointsHistory pointsHistory = PointsHistory.builder()
                .points(points)
                .pointsChanged(10000)  // 변화된 포인트
                .actionType(PointsActionType.SIGNUP_BONUS) // 회원가입 보너스
                .build();

        pointsHistoryRepository.save(pointsHistory);

        // 계좌 번호 자동 생성
        String accountNumber = generateAccountNumber(member.getId());

        SavingsAccount savingsAccount = SavingsAccount.builder()
                .accountNumber(accountNumber)
                .member(member)
                .interestRate(BigDecimal.valueOf(1.50))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7)) // 처음은 7일 계좌
                .status(SavingsAccountStatus.ACTIVE)
                .depositCount(0)
                .totalAmount(0)
                .build();

        savingsAccountRepository.save(savingsAccount);
    }

    private String generateAccountNumber(Long memberId) {
        // 계좌 번호 생성 예시: "회원ID_날짜_순번"
        String memberPrefix = String.format("%08d", memberId); // 회원 ID 8자리 맞추기
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")); // 현재 날짜 (YYMMDD 형식)
        String sequence = String.format("%04d", (int) (Math.random() * 10000)); // 순번 (예시로 000~9999 사이 랜덤)

        return memberPrefix + datePrefix + sequence;
    }

    @Override
    public void updateMember(String username, MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        member.changeName(memberUpdateDTO.getName());
        member.changeNickname(memberUpdateDTO.getNickname());
        member.changePhoneNumber(memberUpdateDTO.getPhoneNumber());
        member.changeEmailAgreed(memberUpdateDTO.getEmailAgreed());

        // 4. 변경 내용 저장
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(String username) {
        // 1. 회원 존재 여부 확인
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. deleted 상태 변경
        if (!member.isDeleted()) {
            member.changeDeleted(true);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("이미 삭제된 회원입니다.");
        }
    }

    @Override
    public MemberViewDTO getMemberInfo(String username) {
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return MemberViewDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .ssn(member.getSsn())
                .emailAgreed(member.isEmailAgreed())
                .socialLinked(member.isSocialLinked())
                .build();
    }

    @Override
    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        // 1. 현재 비밀번호 확인
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 새 비밀번호와 새 비밀번호 확인 일치 여부 확인
        if (!passwordChangeDTO.getPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            throw new RuntimeException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다");
        }

        // 3. 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getPassword());

        // 4. 비밀번호 업데이트
        member.changePassword(encodedPassword);
        memberRepository.save(member);  // 비밀번호 업데이트
    }

    // 중복 체크 서비스 메소드
    @Override
    public Map<String, String> checkDuplicate(Map<String, String> params) {
        Map<String, String> result = new HashMap<>();

        // 아이디 중복 체크
        if (params.containsKey("username") && memberRepository.existsByUsernameAndDeletedFalse(params.get("username"))) {
            result.put("username", "이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 체크
        if (params.containsKey("email") && memberRepository.existsByEmailAndDeletedFalse(params.get("email"))) {
            result.put("email", "이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (params.containsKey("nickname") && memberRepository.existsByNicknameAndDeletedFalse(params.get("nickname"))) {
            result.put("nickname", "이미 존재하는 닉네임입니다.");
        }

        return result;
    }

    // 비밀번호 확인
    @Override
    public boolean verifyPassword(String username, String password) {
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return passwordEncoder.matches(password, member.getPassword());
    }

    // 첫 로그인 시 `firstLoginFlag`를 false로 변경하는 메서드
    @Override
    public void updateFirstLoginFlagToFalse(String username) {
        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (member != null && member.isFirstLoginFlag()) {
            member.changeFirstLoginFlag(false);  // 첫 로그인 플래그를 false로 변경
            memberRepository.save(member);
        }
    }

}
