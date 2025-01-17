package com.moneybricks.stock.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameState {
    private LocalDate date;
    private Map<String, StockPrice> stockPrices;
    private List<News> news;
    private double playerCash;
    private Map<String, Integer> playerStocks;
}