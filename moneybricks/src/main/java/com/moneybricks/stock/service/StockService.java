package com.moneybricks.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;

@Service
public class StockService {
    private final KISClient kisClient;
    private final StockRepository stockRepository;

    @Value("${kis.appkey}")
    private String appKey;

    @Value("${kis.appsecret}")
    private String appSecret;

    @Autowired
    public StockService(KISClient kisClient, StockRepository stockRepository) {
        this.kisClient = kisClient;
        this.stockRepository = stockRepository;
    }

    public List<StockPrice> getCurrentPrices() {
        String token = kisClient.getToken(appKey, appSecret);
        return kisClient.getCurrentPrices(token, Arrays.asList(
                "005930", "000660", "035420", "005380"
        ));
    }

    public void saveTradeHistory(TradeHistory trade) {
        stockRepository.save(trade);
    }
}

