package com.moneybricks.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final NewsService newsService;

    @Autowired
    public StockController(StockService stockService, NewsService newsService) {
        this.stockService = stockService;
        this.newsService = newsService;
    }

    @GetMapping("/current")
    public List<StockPrice> getCurrentPrices() {
        return stockService.getCurrentPrices();
    }

    @GetMapping("/news")
    public NewsResponse generateDailyNews() {
        return newsService.generateDailyNews();
    }

}
