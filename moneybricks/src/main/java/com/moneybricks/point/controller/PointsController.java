package com.moneybricks.point.controller;

import com.moneybricks.point.dto.PointsDTO;
import com.moneybricks.point.service.PointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointsController {

    private final PointsService pointsService;

    // username을 기준으로 포인트 정보 조회
    @GetMapping("/info")
    public ResponseEntity<PointsDTO> getPointsByUsername(Principal principal) {
        String username = principal.getName();  // 현재 로그인한 사용자 이름 얻기
        PointsDTO pointsDTO = pointsService.getPointsByMember(username);
        return ResponseEntity.ok(pointsDTO);
    }
}
