package com.moneybricks.stock.repository;

import com.moneybricks.stock.domain.Player;
import com.moneybricks.stock.domain.PlayerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerStockRepository extends JpaRepository<PlayerStock, Long> {
    List<PlayerStock> findByPlayer(Player player);
//    Optional<PlayerStock> findByPlayerAndStock(Player player);
    Optional<PlayerStock> findByPlayerIdAndStockCode(Long playerId, String stockCode);
}
