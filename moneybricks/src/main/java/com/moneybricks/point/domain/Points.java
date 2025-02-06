package com.moneybricks.point.domain;

import com.moneybricks.common.domain.BaseEntity;
import com.moneybricks.member.domain.Member;
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
@Table(name = "points", indexes = {
        @Index(name = "idx_member_id", columnList = "member_id")
})
public class Points extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 해당 적금을 만든 사용자

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;  // 총 포인트 (사용자 전체 포인트)

    @Column(name = "available_points", nullable = false)
    private Integer availablePoints;  // 사용 가능한 포인트 (잠금되지 않은 포인트)

    @Column(name = "savings_used_points", nullable = false)
    private Integer savingsUsedPoints;  // 적금에 사용된 포인트

    @Column(name = "locked_flag", nullable = false)
    private boolean lockedFlag; // 포인트 잠금 상태 (true: 잠금, false: 사용 가능)

    public void changeTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void changeAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }

    public void changeSavingsUsedPoints(Integer savingsUsedPoints) {
        this.savingsUsedPoints = savingsUsedPoints;
    }

    public void changeLockedFlag(boolean lockedFlag) {
        this.lockedFlag = lockedFlag;
    }
}

