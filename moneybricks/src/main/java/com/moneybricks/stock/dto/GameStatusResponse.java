package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.GameState;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStatusResponse {
    private Long playerId;
    private LocalDate currentDate;
    private double cash;
    private List<PlayerStockResponse> stocks;
    private List<NewsResponse> dailyNews;

    public static GameStatusResponse from(GameState gameState) {
        return GameStatusResponse.builder()
                .playerId(gameState.getPlayer().getId())
                .currentDate(gameState.getPlayer().getCurrentDate())
                .cash(gameState.getPlayer().getCash())
                .stocks(gameState.getStocks().stream()
                        .map(PlayerStockResponse::from)
                        .collect(Collectors.toList()))
                .dailyNews(gameState.getNews().stream()
                        .map(NewsResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
