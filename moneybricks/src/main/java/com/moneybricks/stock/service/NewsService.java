package com.moneybricks.stock.service;

import com.moneybricks.stock.api.GPTApiClient;
import com.moneybricks.stock.component.StockConstants;
import com.moneybricks.stock.domain.News;
import com.moneybricks.stock.domain.Player;
import com.moneybricks.stock.repository.NewsRepository;
import com.moneybricks.stock.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {
    private final NewsRepository newsRepository;
    private final GPTApiClient gptApiClient;
    private final PlayerRepository playerRepository;

    @Transactional
    public List<News> generateDailyNews(LocalDate date) {
        List<News> dailyNews = new ArrayList<>();

        // 시장 전체 뉴스
        dailyNews.add(gptApiClient.generateNews(createMarketNewsPrompt(date)));

        // 산업별 뉴스
        for (String industry : StockConstants.INDUSTRY_STOCKS.keySet()) {
            int newsCount = 1 + (int)(Math.random() * 2);
            for (int i = 0; i < newsCount; i++) {
                dailyNews.add(gptApiClient.generateNews(createIndustryNewsPrompt(date, industry)));
            }
        }

        // 개별 기업 뉴스
        int companyNewsCount = 2 + (int)(Math.random() * 2);
        for (int i = 0; i < companyNewsCount; i++) {
            dailyNews.add(gptApiClient.generateNews(createCompanyNewsPrompt(date)));
        }

        // 생성된 뉴스 저장
        return newsRepository.saveAll(dailyNews);
    }

    public List<News> getDailyNews(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFountException("플레이어를 찾을 수 없습니다."));

        return newsRepository.findByDateOrderByTypeDesc(player.getCurrentDate());
    }
}
