package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.Stock;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponse {
    private String code;
    private String name;
    private String industry;

    public static StockResponse from(Stock stock) {
        return StockResponse.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .industry(stock.getIndustry())
                .build();
    }
}