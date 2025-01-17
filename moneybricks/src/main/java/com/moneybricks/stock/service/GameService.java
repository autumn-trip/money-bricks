package com.moneybricks.stock.service;

import com.moneybricks.stock.Exception.InsufficientFundsException;
import com.moneybricks.stock.Exception.PlayerNotFoundException;
import com.moneybricks.stock.domain.*;
import com.moneybricks.stock.dto.SimulationResult;
import com.moneybricks.stock.repository.PlayerRepository;
import com.moneybricks.stock.repository.PlayerStockRepository;
import com.moneybricks.stock.repository.StockRepository;
import com.moneybricks.stock.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {
    private final PlayerRepository playerRepository;
    private final StockService stockService;
    private final NewsService newsService;
    private final PlayerStockRepository playerStockRepository;
    private final DailyStockGame dailyStockGame;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final StockRepository stockRepository;

    public Player startNewGame(double initialCash) {
        Player player = new Player();
        player.setGameStartDate(LocalDate.now());
        player.setCurrentDate(LocalDate.now());
        player.setCash(initialCash);

        // 게임 초기화
        dailyStockGame.initializeGame(player.getCurrentDate());

        return playerRepository.save(player);
    }

    public GameState processNextDay(Long playerId) {
        Player player = getPlayer(playerId);

        // 게임 시뮬레이션 실행
        SimulationResult simulationResult = dailyStockGame.simulateNextDay();

        // 플레이어 상태 업데이트
        player.setCurrentDate(simulationResult.getDate());
        playerRepository.save(player);

        return createGameState(player, simulationResult);
    }

    @Transactional
    public TradeResult trade(Long playerId, String stockCode, int quantity, TradeType type) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("플레이어를 찾을 수 없습니다."));

        StockPrice currentPrice = dailyStockGame.getCurrentPrice(stockCode);

        return type == TradeType.BUY ?
                executeBuy(player, stockCode, quantity, currentPrice) :
                executeSell(player, stockCode, quantity, currentPrice);
    }


    private GameState createGameState(Player player, SimulationResult result) {
        return GameState.builder()
                .date(result.getDate())
                .stockPrices(result.getStockPrices())
                .news(result.getNews())
                .playerCash(player.getCash())
                .playerStocks(getPlayerStocks(player))
                .build();
    }

    private Map<String, Integer> getPlayerStocks(Player player) {
        return playerStockRepository.findByPlayer(player).stream()
                .collect(Collectors.toMap(
                        ps -> ps.getStock().getCode(),
                        PlayerStock::getQuantity
                ));
    }


    private TradeResult executeBuy(Player player, String stockCode, int quantity, StockPrice stockPrice) {
        double totalCost = stockPrice.getCurrentPrice() * quantity;

        if (player.getCash() < totalCost) {
            throw new InsufficientFundsException("잔액이 부족합니다.");
        }

        PlayerStock playerStock = playerStockRepository.findByPlayerIdAndStockCode(player.getId(), stockCode)
                .orElse(PlayerStock.builder()
                        .player(player.getId())
                        .stockCode(stockCode)
                        .stockName(stockPrice.getName())
                        .quantity(0)
                        .averagePrice(0.0)
                        .build());


        // 평균 매수가 계산
        double newTotalCost = (playerStock.getQuantity() * playerStock.getAveragePrice()) + totalCost;
        int newTotalQuantity = playerStock.getQuantity() + quantity;
        playerStock.setAveragePrice(newTotalCost / newTotalQuantity);
        playerStock.setQuantity(newTotalQuantity);

        player.setCash(player.getCash() - totalCost);

        // 거래 기록 저장
        TradeHistory tradeHistory = TradeHistory.builder()
                .playerId(player.getId())
                .code(stockCode)
                .stockName(stockPrice.getName())
                .tradeDate(LocalDateTime.now())
                .type(TradeType.BUY)
                .quantity(quantity)
                .price(stockPrice.getCurrentPrice())
                .totalAmount(totalCost)
                .build();

        playerStockRepository.save(playerStock);
        tradeHistoryRepository.save(tradeHistory);
        playerRepository.save(player);

        return new TradeResult(true, "매수가 완료되었습니다.", totalCost, player.getCash());
    }
}