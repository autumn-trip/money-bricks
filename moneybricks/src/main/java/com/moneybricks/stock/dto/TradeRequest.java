package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.TradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {
    private Long PlayerId;
    private String stockCode;
    private int quantity;
    private TradeType type;
}
