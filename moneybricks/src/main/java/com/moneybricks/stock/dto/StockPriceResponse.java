package com.moneybricks.stock.dto;


import com.moneybricks.stock.domain.StockPrice;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPriceResponse {
    private String stockCode;
    private String stockName;
    private double currentPrice;
    private double priceChange;

    public static StockPriceResponse from(StockPrice stockPrice) {
        return StockPriceResponse.builder()
                .stockCode(stockPrice.getStock().getCode())
                .stockName(stockPrice.getStock().getName())
                .currentPrice(stockPrice.getCurrentPrice())
                .priceChange(stockPrice.getPriceChange())
                .build();
    }
}