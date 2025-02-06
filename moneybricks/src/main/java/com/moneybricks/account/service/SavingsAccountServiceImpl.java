package com.moneybricks.account.service;

import com.moneybricks.account.domain.*;
import com.moneybricks.account.dto.DepositCreateDTO;
import com.moneybricks.account.dto.SavingsAccountDTO;
import com.moneybricks.account.repository.AccountHistoryRepository;
import com.moneybricks.account.repository.DepositHistoryRepository;
import com.moneybricks.account.repository.SavingsAccountRepository;
import com.moneybricks.point.domain.Points;
import com.moneybricks.point.domain.PointsActionType;
import com.moneybricks.point.domain.PointsHistory;
import com.moneybricks.point.repository.PointsHistoryRepository;
import com.moneybricks.point.repository.PointsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final DepositHistoryRepository depositHistoryRepository;
    private final PointsHistoryRepository  pointsHistoryRepository;
    private final PointsRepository pointsRepository;

    private static final BigDecimal BASE_INTEREST_RATE = new BigDecimal("1.50");  // 기본금리
    private static final BigDecimal DAILY_BONUS_RATE = new BigDecimal("0.10");    // daily deposit bonus

    // 계좌 조회
    @Override
    public SavingsAccountDTO getSavingsAccount(String username) {

        SavingsAccount savingsAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        return SavingsAccountDTO.builder()
                .id(savingsAccount.getId())
                .memberId(savingsAccount.getMember().getId())
                .accountNumber(savingsAccount.getAccountNumber())
                .interestRate(savingsAccount.getInterestRate())
                .depositCount(savingsAccount.getDepositCount())
                .startDate(savingsAccount.getStartDate())
                .endDate(savingsAccount.getEndDate())
                .totalAmount(savingsAccount.getTotalAmount())
                .status(String.valueOf(savingsAccount.getStatus()))
                .build();
    }

    // 로그인 성공 시 호출될 만기 여부 확인 메소드
    @Override
    public boolean checkMaturityAndProcess(String username) {

        SavingsAccount savingsAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        if (savingsAccount.getStatus() == SavingsAccountStatus.ACTIVE &&
                !savingsAccount.getEndDate().isAfter(LocalDate.now())) { // 오늘 이전이면 처리
            handleMaturity(savingsAccount);
            return true; // 만기 처리됨
        }

        return false; // 만기 처리되지 않은 경우
    }

    // 계좌 갱신
    @Override
    public void renewSavingsAccount(String username, int renewalPeriod) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        // 계좌가 해지(중도 해지 또는 만기)되지 않았으면 갱신 불가능
        if (savingsAccount.getStatus() == SavingsAccountStatus.ACTIVE) {
            throw new IllegalStateException("활성화된 계좌는 갱신할 수 없습니다. 먼저 해지해야 합니다.");
        }

        savingsAccount.renewAccount(renewalPeriod);

        savingsAccountRepository.save(savingsAccount);
    }

    // 중도 해지
    @Override
    public void cancelSavingsAccount(String username) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        if (savingsAccount.getStatus() != SavingsAccountStatus.ACTIVE) {
            throw new RuntimeException("활성 상태의 계좌만 중도해지가 가능합니다.");
        }

        // 이자율 절반으로 감소
        BigDecimal reducedInterestRate = savingsAccount.getInterestRate().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        savingsAccount.changeInterestRate(reducedInterestRate);

        // 총 지급할 포인트 계산 (이자 포함)
        Integer totalAmountWithInterest = calculateTotalMaturityPoints(savingsAccount);

        // 포인트 지급 및 포인트 히스토리 저장
        processPointTransaction(savingsAccount, totalAmountWithInterest, PointsActionType.EARLY_WITHDRAWAL);

        // 히스토리 저장 (중도 해지)
        saveAccountHistory(savingsAccount, totalAmountWithInterest, SavingsAccountStatus.CANCELED);

        // 계좌 상태 변경
        savingsAccount.changeStatus(SavingsAccountStatus.CANCELED);
        savingsAccountRepository.save(savingsAccount);
    }

    // 입금 기능 (하루 1회 제한)
    @Override
    public void deposit(String username, DepositCreateDTO depositCreateDTO) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 계좌를 찾을 수 없습니다."));

        Integer amount = depositCreateDTO.getDepositAmount();

        // 1. 계좌가 활성화 상태인지 확인
        if (savingsAccount.getStatus() != SavingsAccountStatus.ACTIVE) {
            throw new IllegalStateException("활성화된 계좌에만 입금할 수 있습니다.");
        }

        // 2. 하루 1회 입금 제한 체크 (정확한 시간 범위 적용)
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        boolean hasDepositedToday = depositHistoryRepository.existsBySavingsAccountAndDepositDateBetween(
                savingsAccount, startOfDay, endOfDay);

        if (hasDepositedToday) {
            throw new IllegalStateException("하루에 한 번만 입금할 수 있습니다.");
        }

        // 3. 포인트 정보 조회
        Points points = pointsRepository.findByMember(savingsAccount.getMember());

        // 4. 사용 가능한 포인트 확인 (포인트 부족 시 예외 발생)
        if (points.getAvailablePoints() < amount) {
            throw new IllegalStateException("사용 가능한 포인트가 부족합니다.");
        }

        // 5. 계좌 잔액 업데이트
        savingsAccount.changeTotalAmount(savingsAccount.getTotalAmount() + amount);

        // 6. 입금 횟수 증가
        savingsAccount.incrementDepositCount();

        // 7. 이자율 업데이트 (입금 횟수 증가 반영)
        updateInterestRate(savingsAccount);

        // 8. 입금 내역 저장
        DepositHistory depositHistory = DepositHistory.builder()
                .savingsAccount(savingsAccount)
                .depositAmount(amount)
                .depositDate(LocalDateTime.now())  // 현재 시간 저장
                .balanceAfterDeposit(savingsAccount.getTotalAmount())
                .build();

        depositHistoryRepository.save(depositHistory);

        // 9. 포인트 상태 업데이트
        points.changeSavingsUsedPoints(points.getSavingsUsedPoints() + amount);
        points.changeAvailablePoints(points.getAvailablePoints() - amount);

        // 10. 포인트 히스토리 저장
        PointsHistory pointsHistory = PointsHistory.builder()
                .points(points)
                .pointsChanged(-amount)
                .actionType(PointsActionType.DEPOSIT)
                .build();

        pointsHistoryRepository.save(pointsHistory);

        // 11. 변경된 데이터 저장
        savingsAccountRepository.save(savingsAccount);
        pointsRepository.save(points);
    }

    // 만기 처리
    private void handleMaturity(SavingsAccount savingsAccount) {

        // 총 지급할 포인트 계산 (이자 포함)
        Integer totalAmountWithInterest = calculateTotalMaturityPoints(savingsAccount);

        // 포인트 지급 및 포인트 히스토리 저장
        processPointTransaction(savingsAccount, totalAmountWithInterest, PointsActionType.MATURITY_WITHDRAWAL);

        // 계좌 이력 기록
        saveAccountHistory(savingsAccount, totalAmountWithInterest, SavingsAccountStatus.COMPLETED);

        // 상태 변경
        savingsAccount.changeStatus(SavingsAccountStatus.COMPLETED);

        savingsAccountRepository.save(savingsAccount);
    }

    // 포인트 지급 및 히스토리 저장
    private void processPointTransaction(SavingsAccount savingsAccount, Integer totalAmountWithInterest, PointsActionType actionType) {

        // 원금 포인트 (적금에 넣었던 포인트)
        Integer principalAmount = savingsAccount.getTotalAmount();

        // 지급할 이자
        Integer interestAmount = totalAmountWithInterest - principalAmount;

        // 포인트 지급 로직
        Points points = pointsRepository.findByMember(savingsAccount.getMember());

        // 총 포인트 (이자만 추가)
        points.changeTotalPoints(points.getTotalPoints() + interestAmount);

        // 즉시 사용 가능 포인트 (원금 + 이자 지급)
        points.changeAvailablePoints(points.getAvailablePoints() + totalAmountWithInterest);

        // 사용된 적금 포인트 초기화
        points.changeSavingsUsedPoints(0);

        pointsRepository.save(points);

        savePointsHistory(points, totalAmountWithInterest, actionType);
    }

    // 포인트 히스토리 저장
    private void savePointsHistory(Points points, Integer pointsChanged, PointsActionType actionType) {
        PointsHistory pointsHistory = PointsHistory.builder()
                .points(points)
                .pointsChanged(pointsChanged)
                .actionType(actionType)
                .build();

        pointsHistoryRepository.save(pointsHistory);
    }

    // 계좌 히스토리 저장
    private void saveAccountHistory(SavingsAccount savingsAccount, Integer totalAmountWithInterest, SavingsAccountStatus status) {
        AccountHistory accountHistory = AccountHistory.builder()
                .savingsAccount(savingsAccount)
                .previousMaturityPoints(totalAmountWithInterest)
                .previousInterestRate(savingsAccount.getInterestRate())
                .previousDepositCount(savingsAccount.getDepositCount())
                .previousStatus(status)
                .previousStartDate(savingsAccount.getStartDate())
                .previousEndDate(LocalDate.now())
                .build();

        accountHistoryRepository.save(accountHistory);
    }

    // 이자 포함 포인트 계산
    private Integer calculateTotalMaturityPoints(SavingsAccount savingsAccount) {
        // 이자 포함 계산: principal * (1 + interestRate / 100)
        BigDecimal totalPointsWithInterest = BigDecimal.valueOf(savingsAccount.getTotalAmount())
                .multiply(BigDecimal.ONE.add(savingsAccount.getInterestRate().divide(BigDecimal.valueOf(100))));

        // 반올림 처리 후 int 로 변환
        return totalPointsWithInterest.setScale(0, RoundingMode.HALF_UP).intValue(); // 소수점 반올림
    }

    private void updateInterestRate(SavingsAccount account) {
        // 기본금리 + 매일 입금 보너스
        BigDecimal totalRate = BASE_INTEREST_RATE.add(DAILY_BONUS_RATE);

        // 입금 횟수에 따른 보너스 이자율 계산
        BigDecimal bonusRate = calculateBonusRate(account.getDepositCount());

        // 최종 이자율 = 기본금리 + 매일 입금 보너스 + 누적 보너스
        totalRate = totalRate.add(bonusRate);
        account.changeInterestRate(totalRate);
    }

    private BigDecimal calculateBonusRate(Integer depositCount) {
        if (depositCount >= 30) return new BigDecimal("2.50");
        if (depositCount >= 25) return new BigDecimal("1.50");
        if (depositCount >= 20) return new BigDecimal("1.00");
        if (depositCount >= 15) return new BigDecimal("0.70");
        if (depositCount >= 10) return new BigDecimal("0.40");
        if (depositCount >= 5) return new BigDecimal("0.20");
        return BigDecimal.ZERO;
    }
}
