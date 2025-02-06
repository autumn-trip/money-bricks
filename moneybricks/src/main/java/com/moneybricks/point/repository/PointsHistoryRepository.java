package com.moneybricks.point.repository;

import com.moneybricks.point.domain.PointsHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointsHistoryRepository extends JpaRepository<PointsHistory, Integer> {
    // username을 기준으로 PointsHistory 조회 (엔티티 그래프 사용)
    @EntityGraph(attributePaths = {"points", "points.member"})
    List<PointsHistory> findByPointsMemberUsername(String username);
}
