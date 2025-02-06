package com.moneybricks.point.controller;

import com.moneybricks.point.dto.PointsHistoryDTO;
import com.moneybricks.point.service.PointsHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/points/history")
@RequiredArgsConstructor
public class PointsHistoryController {

    private final PointsHistoryService pointsHistoryService;

    // username을 기준으로 포인트 변화 내역 조회
    @GetMapping("/list")
    public ResponseEntity<List<PointsHistoryDTO>> getPointsHistory(Principal principal) {
        String username = principal.getName();
        List<PointsHistoryDTO> pointsHistoryList = pointsHistoryService.getPointsHistory(username);
        return ResponseEntity.ok(pointsHistoryList);
    }
}
