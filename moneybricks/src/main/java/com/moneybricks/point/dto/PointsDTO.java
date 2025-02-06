package com.moneybricks.point.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsDTO {
    private Long id;                       // 포인트 ID
    private Long memberId;                 // 회원 ID
    private Integer totalPoints;           // 총 포인트
    private Integer availablePoints;       // 사용 가능한 포인트
    private Integer savingsUsedPoints;     // 적금에 사용된 포인트
    private boolean lockedFlag;            // 포인트 잠금 상태
}
