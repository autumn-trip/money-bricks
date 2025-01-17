package com.moneybricks.stock.repository;

import com.moneybricks.stock.domain.Stock;
import com.moneybricks.stock.domain.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    List<StockPrice> findByDateOrderByStockCode(LocalDate date);
    Optional<StockPrice> findByStockAndDate(Stock stock, LocalDate date);
}
