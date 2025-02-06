package com.moneybricks.point.domain;


import com.moneybricks.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "points_history", indexes = {
        @Index(name = "idx_points_id", columnList = "points_id"),
        @Index(name = "idx_action_type", columnList = "action_type"),
})
public class PointsHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_id", nullable = false)
    private Points points;  // 포인트 객체 참조

    @Column(name = "points_changed", nullable = false)
    private Integer pointsChanged;  // 변화된 포인트 (양수 또는 음수)

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private PointsActionType actionType;  // 포인트 변화 유형 (Enum)
}

