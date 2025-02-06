package com.moneybricks.common.controller;

import com.moneybricks.common.util.CustomJWTException;
import com.moneybricks.common.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    // refresh 메서드
    @RequestMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
        // refreshToken이 null일 경우 예외 처리
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        // Authorization 헤더가 비정상일 경우 예외 처리
        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        // accessToken을 Authorization 헤더에서 추출
        String accessToken = authHeader.substring(7);

        // Access 토큰이 만료되지 않았다면 그대로 반환
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // refreshToken을 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh ... claims: " + claims);

        // 새로운 accessToken을 생성
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        // refreshToken의 만료 시간이 1시간 미만이라면 새로운 refreshToken을 생성
        String newRefreshToken = checkTime((Integer) claims.get("exp"))
                ? JWTUtil.generateToken(claims, 60 * 24) // 1일 동안 유효한 refreshToken 생성
                : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    // JWT의 만료 시간(exp)을 기준으로 1시간 미만 남았다면 새로운 refreshToken을 생성
    private boolean checkTime(Integer exp) {
        java.util.Date expDate = new java.util.Date((long) exp * (1000)); // exp를 밀리세컨드로 변환
        long gap = expDate.getTime() - System.currentTimeMillis(); // 현재 시간과 만료 시간 차이
        long leftMin = gap / (1000 * 60); // 남은 시간(분 단위)
        return leftMin < 60; // 1시간 미만이라면 true
    }

    // Access Token이 만료된 경우 처리
    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}
