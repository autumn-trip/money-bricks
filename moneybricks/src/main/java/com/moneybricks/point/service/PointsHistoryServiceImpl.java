package com.moneybricks.point.service;

import com.moneybricks.point.domain.PointsHistory;
import com.moneybricks.point.dto.PointsHistoryDTO;
import com.moneybricks.point.repository.PointsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class PointsHistoryServiceImpl implements PointsHistoryService {
    private final PointsHistoryRepository pointsHistoryRepository;

    // 특정 포인트의 변화 내역 조회
    @Override
    public List<PointsHistoryDTO> getPointsHistory(String username) {

        List<PointsHistory> pointsHistoryList = pointsHistoryRepository.findByPointsMemberUsername(username);

        // 포인트 내역을 DTO로 변환하여 반환
        return pointsHistoryList.stream()
                .map(pointsHistory -> PointsHistoryDTO.builder()
                        .id(pointsHistory.getId())
                        .pointsId(pointsHistory.getPoints().getId())
                        .pointsChanged(pointsHistory.getPointsChanged())
                        .actionType(pointsHistory.getActionType().name())
                        .createdAt(pointsHistory.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

}
