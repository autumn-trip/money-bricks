package com.moneybricks.stock.repository;

import com.moneybricks.stock.domain.Player;
import com.moneybricks.stock.domain.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
    List<TradeHistory> findByPlayerOrderByTradeDateDesc(Player player);
    List<TradeHistory> findByPlayerAAndTradeDateBetween(
            Player player, LocalDateTime startDate, LocalDateTime endDate);

}
