package com.moneybricks.point.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsHistoryDTO {
    private Long id;  // 포인트 내역 ID
    private Long pointsId;  // 해당 포인트 ID
    private Integer pointsChanged;  // 변화된 포인트
    private String actionType;  // 포인트 변화 유형
    private LocalDateTime createdAt;  // 내역 생성 시간 (BaseEntity에서 상속)
}
