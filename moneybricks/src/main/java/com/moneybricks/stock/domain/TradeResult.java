package com.moneybricks.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeResult {
    private boolean success;
    private String message;
    private double amount;
    private double remainingCash;
}