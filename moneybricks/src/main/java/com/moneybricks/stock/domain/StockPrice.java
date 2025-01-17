package com.moneybricks.stock.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPrice {
    private String code;        // 종목 코드를 직접 문자열로 저장
    private String name;
    private double currentPrice;
    private double priceChange;
    private String industry;
    private LocalDateTime lastUpdated;
}