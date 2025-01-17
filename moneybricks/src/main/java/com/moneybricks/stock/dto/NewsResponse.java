package com.moneybricks.stock.dto;

import com.moneybricks.stock.domain.News;
import com.moneybricks.stock.domain.NewsType;
import com.moneybricks.stock.domain.Stock;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private NewsType type;
    private double sentimentScore;
    private List<String> affectedStockCodes;

    public static NewsResponse from(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .type(news.getType())
                .sentimentScore(news.getSentimentScore())
                .affectedStockCodes(news.getAffectedStocks().stream()
                        .map(Stock::getCode)
                        .collect(Collectors.toList()))
                .build();
    }
}