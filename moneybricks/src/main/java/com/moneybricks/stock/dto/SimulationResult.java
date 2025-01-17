package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.News;
import com.moneybricks.stock.domain.StockPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {
    private LocalDate date;
    private Map<String, StockPrice> stockPrices;
    private List<News> news;
}