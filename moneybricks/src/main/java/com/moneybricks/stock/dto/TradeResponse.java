package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.TradeResult;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeResponse {
    private boolean success;
    private String message;
    private double amount;
    private double remainingCash;

    public static TradeResponse from(TradeResult result) {
        return TradeResponse.builder()
                .success(result.isSuccess())
                .message(result.getMessage())
                .amount(result.getAmount())
                .remainingCash(result.getRemainingCash())
                .build();
    }
}