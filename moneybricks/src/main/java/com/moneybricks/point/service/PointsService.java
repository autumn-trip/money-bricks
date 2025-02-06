package com.moneybricks.point.service;

import com.moneybricks.point.dto.PointsDTO;

public interface PointsService {
    // 특정 사용자의 포인트 조회
    PointsDTO getPointsByMember(String username);
}
