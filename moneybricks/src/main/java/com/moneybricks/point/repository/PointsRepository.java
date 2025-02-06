package com.moneybricks.point.repository;

import com.moneybricks.member.domain.Member;
import com.moneybricks.point.domain.Points;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointsRepository extends JpaRepository<Points, Integer> {
    Points findByMember(Member member);
}
