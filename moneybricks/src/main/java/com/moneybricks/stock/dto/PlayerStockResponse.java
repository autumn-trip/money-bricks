package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.PlayerStock;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStockResponse {
    private String stockCode;
    private String stockName;
    private int quantity;
    private double averagePrice;
    private double currentPrice;
    private double totalReturn;  // 평가손익
    private double returnRate;   // 수익률

    public static PlayerStockResponse from(PlayerStock playerStock) {
        double currentPrice = playerStock.getStock().getPriceHistory().get(0).getCurrentPrice();
        double totalReturn = (currentPrice - playerStock.getAveragePrice()) * playerStock.getQuantity();
        double returnRate = ((currentPrice / playerStock.getAveragePrice()) - 1) * 100;

        return PlayerStockResponse.builder()
                .stockCode(playerStock.getStock().getCode())
                .stockName(playerStock.getStock().getName())
                .quantity(playerStock.getQuantity())
                .averagePrice(playerStock.getAveragePrice())
                .currentPrice(currentPrice)
                .totalReturn(totalReturn)
                .returnRate(returnRate)
                .build();
    }
}
