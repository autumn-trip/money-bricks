package com.moneybricks.repository;

import com.moneybricks.account.domain.SavingsAccount;
import com.moneybricks.account.domain.SavingsAccountStatus;
import com.moneybricks.account.repository.SavingsAccountRepository;
import com.moneybricks.member.domain.Member;
import com.moneybricks.member.domain.MemberRole;
import com.moneybricks.member.repository.MemberRepository;
import com.moneybricks.point.domain.Points;
import com.moneybricks.point.domain.PointsActionType;
import com.moneybricks.point.domain.PointsHistory;
import com.moneybricks.point.repository.PointsHistoryRepository;
import com.moneybricks.point.repository.PointsRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Log4j2
@Transactional
@Commit
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointsRepository pointsRepository;

    @Autowired
    private PointsHistoryRepository pointsHistoryRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {

        for (int i = 0; i < 10 ; i++) {
            // 회원 생성
            Member member = Member.builder()
                    .username("user" + i)
                    .email("user" + i + "@aaa.com")
                    .password(passwordEncoder.encode("1111"))
                    .name("user" + i)
                    .nickname("USER" + i)
                    .phoneNumber("0100000000" + i)
                    .ssn("01010" + i + "4")
                    .deleted(false)
                    .firstLoginFlag(true)
                    .build();

            member.addRole(MemberRole.USER);

            if (i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }

            Member savedMember = memberRepository.save(member);

            // 포인트 지급
            Points points = Points.builder()
                    .member(savedMember)
                    .totalPoints(10000)  // 초기 포인트 10,000
                    .availablePoints(10000)
                    .savingsUsedPoints(0)
                    .lockedFlag(true) // 초기 포인트는 포인트 샵에서 사용 불가
                    .build();

            Points savedPoints = pointsRepository.save(points);

            // 포인트 히스토리 저장
            PointsHistory pointsHistory = PointsHistory.builder()
                    .points(savedPoints)
                    .pointsChanged(10000)  // 변화된 포인트
                    .actionType(PointsActionType.SIGNUP_BONUS) // 회원가입 보너스
                    .build();

            pointsHistoryRepository.save(pointsHistory);

            // 적금 계좌 생성
            // 계좌 번호 생성 예시: "회원ID_날짜_순번"
            String memberPrefix = String.format("%08d", member.getId()); // 회원 ID 8자리 맞추기
            String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")); // 현재 날짜 (YYMMDD 형식)
            String sequence = String.format("%04d", (int) (Math.random() * 10000)); // 순번 (예시로 000~9999 사이 랜덤)

            String accountNumber = memberPrefix + datePrefix + sequence;

            SavingsAccount savingsAccount = SavingsAccount.builder()
                    .accountNumber(accountNumber)
                    .member(savedMember)
                    .interestRate(BigDecimal.valueOf(1.50))
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(14))
                    .status(SavingsAccountStatus.ACTIVE)
                    .depositCount(0)
                    .totalAmount(0)
                    .build();

            SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);

            // ✅ 검증 로그
            log.info("✔ Saved Member: " + savedMember);
            log.info("✔ Saved Points: " + savedPoints);
            log.info("✔ Saved Points History: " + pointsHistory);
            log.info("✔ Saved Savings Account: " + savedAccount);
        }
    }
}