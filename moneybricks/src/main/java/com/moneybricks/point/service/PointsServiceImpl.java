package com.moneybricks.point.service;

import com.moneybricks.member.domain.Member;
import com.moneybricks.member.repository.MemberRepository;
import com.moneybricks.point.domain.Points;
import com.moneybricks.point.dto.PointsDTO;
import com.moneybricks.point.repository.PointsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class PointsServiceImpl implements PointsService {
    private final PointsRepository pointsRepository;
    private final MemberRepository memberRepository;

    // 특정 사용자의 포인트 조회
    @Override
    public PointsDTO getPointsByMember(String username) {

        Member member = memberRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 해당 회원의 포인트 정보 조회
        Points points = pointsRepository.findByMember(member);

        // 포인트 생성일로부터 7일이 지나면 lockedFlag를 false로 설정
        if (points.getCreatedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            points.changeLockedFlag(false);
            pointsRepository.save(points);  // 변경사항 저장
        }

        return new PointsDTO(
                points.getId(),
                points.getMember().getId(),
                points.getTotalPoints(),
                points.getAvailablePoints(),
                points.getSavingsUsedPoints(),
                points.isLockedFlag()
        );
    }
}
