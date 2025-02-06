package com.moneybricks.point.service;

import com.moneybricks.point.dto.PointsHistoryDTO;

import java.util.List;

public interface PointsHistoryService {
    // 특정 포인트의 변화 내역 조회
    List<PointsHistoryDTO> getPointsHistory(String username);
}
