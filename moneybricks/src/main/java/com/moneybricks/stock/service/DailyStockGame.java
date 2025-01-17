package com.moneybricks.stock.service;

import com.moneybricks.stock.api.GPTApiClient;
import com.moneybricks.stock.api.KisApiClient;
import com.moneybricks.stock.component.StockConstants;
import com.moneybricks.stock.domain.News;
import com.moneybricks.stock.domain.Stock;
import com.moneybricks.stock.domain.StockPrice;
import com.moneybricks.stock.dto.SimulationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStockGame {
    private final KisApiClient kisApiClient;
    private final GPTApiClient gptApiClient;
    private LocalDate currentDate;
    private Map<String, StockPrice> stockPrices = new HashMap<>();
    private List<News> dailyNews = new ArrayList<>();
    private final Random random = new Random();

    private double playerCash;
    private Map<String, Integer> playerStocks = new HashMap<>();

    public void initializeGame(LocalDate startDate) {
        this.currentDate = startDate;

        // 첫날의 주가만 실제 KIS API에서 가져오기
        for (List<Stock> stocks : StockConstants.INDUSTRY_STOCKS.values()) {
            for (Stock stock : stocks) {
                double initialPrice = 50000; // 기본값
                try {
                    initialPrice = kisApiClient.getCurrentPrice(stock.getCode());
                } catch (Exception e) {
                    log.error("Failed to get initial price for " + stock.getCode(), e);
                }

                StockPrice stockPrice = StockPrice.builder()
                        .code(stock.getCode())
                        .name(stock.getName())
                        .currentPrice(initialPrice)
                        .priceChange(0.0)
                        .industry(stock.getIndustry())
                        .lastUpdated(currentDate.atTime(15, 30))
                        .build();

                stockPrices.put(stock.getCode(), stockPrice);
            }
        }
    }

    public SimulationResult simulateNextDay() {
        currentDate = currentDate.plusDays(1);
        generateDailyNews();
        double marketSentiment = generateMarketSentiment();
        calculateNewPrices(marketSentiment);

        return SimulationResult.builder()
                .date(currentDate)
                .stockPrices(new HashMap<>(stockPrices))
                .news(new ArrayList<>(dailyNews))
                .build();
    }

    public StockPrice getCurrentPrice(String stockCode) {
        return stockPrices.get(stockCode);
    }

    private void generateDailyNews() {
        dailyNews.clear();

        // 1. 전체 시장 뉴스 (1개)
        dailyNews.add(generateMarketNews());

        // 2. 산업별 뉴스 (각 1-2개)
        for (String industry : StockConstants.INDUSTRY_STOCKS.keySet()) {
            int newsCount = 1 + random.nextInt(2);
            for (int i = 0; i < newsCount; i++) {
                dailyNews.add(generateIndustryNews(industry));
            }
        }

        // 3. 개별 기업 뉴스 (2-3개)
        int companyNewsCount = 2 + random.nextInt(2);
        for (int i = 0; i < companyNewsCount; i++) {
            dailyNews.add(generateCompanyNews());
        }
    }

    private double generateMarketSentiment() {
        // -0.2 ~ 0.2 사이의 시장 전반적인 움직임 생성
        return (random.nextDouble() - 0.5) * 0.4;
    }

    private void calculateNewPrices(double marketSentiment) {
        // 각 주식별로 가격 변동 계산
        for (StockPrice stock : stockPrices.values()) {
            double totalEffect = marketSentiment;

            // 1. 뉴스 효과 반영
            for (News news : dailyNews) {
                if (news.getAffectedStocks().contains(stock.getCode())) {  // stock.getStock().getCode() -> stock.getCode()
                    double newsEffect = news.getSentimentScore() * 0.05; // 최대 5% 영향
                    totalEffect += newsEffect;
                }
            }

            // 2. 랜덤 노이즈 추가 (-1% ~ 1%)
            double noise = (random.nextDouble() - 0.5) * 0.02;
            totalEffect += noise;

            // 3. 새로운 가격 계산
            double oldPrice = stock.getCurrentPrice();
            double priceChange = oldPrice * totalEffect;
            double newPrice = Math.max(1000, oldPrice + priceChange); // 최소가격 1000원

            // 4. 가격 변동 제한 (+- 30% 이내)
            double maxChange = oldPrice * 0.3;
            newPrice = Math.max(oldPrice - maxChange, Math.min(oldPrice + maxChange, newPrice));

            // 5. 가격 업데이트
            stock.setCurrentPrice(Math.round(newPrice * 100) / 100.0); // 소수점 2자리까지
            stock.setPriceChange((newPrice - oldPrice) / oldPrice * 100);
            stock.setLastUpdated(currentDate.atTime(15, 30));
        }
    }

    private News generateMarketNews() {
        String prompt = String.format("""
            전체 주식 시장에 대한 뉴스를 생성해주세요.
            날짜: %s
            
            다음 형식으로 작성해주세요:
            - 제목: [뉴스 제목]
            - 내용: [뉴스 내용]
            - 영향을 받는 기업: [ALL]
            - 감정점수: [-1.0 ~ 1.0]
            """,
                currentDate
        );

        return gptApiClient.generateNews(prompt);
    }

    private News generateIndustryNews(String industry) {
        String prompt = String.format("""
            %s 산업에 대한 뉴스를 생성해주세요.
            날짜: %s
            해당 산업 기업들: %s
            
            다음 형식으로 작성해주세요:
            - 제목: [뉴스 제목]
            - 내용: [뉴스 내용]
            - 영향을 받는 기업: [기업코드들]
            - 감정점수: [-1.0 ~ 1.0]
            """,
                industry,
                currentDate,
                StockConstants.INDUSTRY_STOCKS.get(industry).stream()
                        .map(s -> s.getName() + "(" + s.getCode() + ")")
                        .collect(Collectors.joining(", "))
        );

        return gptApiClient.generateNews(prompt);
    }

    private News generateCompanyNews() {
        // 랜덤하게 한 기업 선택
        List<Stock> allStocks = StockConstants.INDUSTRY_STOCKS.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Stock targetStock = allStocks.get(random.nextInt(allStocks.size()));

        String prompt = String.format("""
            %s(%s)에 대한 개별 기업 뉴스를 생성해주세요.
            날짜: %s
            
            다음 형식으로 작성해주세요:
            - 제목: [뉴스 제목]
            - 내용: [뉴스 내용]
            - 영향을 받는 기업: [%s]
            - 감정점수: [-1.0 ~ 1.0]
            """,
                targetStock.getName(),
                targetStock.getCode(),
                currentDate,
                targetStock.getCode()
        );

        return gptApiClient.generateNews(prompt);
    }
}