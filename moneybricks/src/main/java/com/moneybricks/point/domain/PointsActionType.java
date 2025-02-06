package com.moneybricks.point.domain;

public enum PointsActionType {
    SIGNUP_BONUS,  // 회원가입 보너스
    DEPOSIT,       // 적금 입금
    EARLY_WITHDRAWAL,  // 중도 해지 (적금 출금)
    MATURITY_WITHDRAWAL, // 만기 해지 (적금 출금)
    PURCHASE,      // 포인트샵 구매
    EVENT_REWARD;  // 이벤트 보상
}

