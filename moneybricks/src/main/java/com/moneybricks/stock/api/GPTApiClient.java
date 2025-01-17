package com.moneybricks.stock.api;

import com.moneybricks.stock.domain.News;
import com.moneybricks.stock.domain.NewsType;
import com.moneybricks.stock.domain.Stock;
import com.moneybricks.stock.dto.ChatCompletionRequest;
import com.moneybricks.stock.dto.ChatCompletionResponse;
import com.moneybricks.stock.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GPTApiClient {
    private final WebClient gptWebClient;

    public News generateNews(String prompt) {
        try {
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(List.of(
                            new Message("system", "You are a financial news generator."),
                            new Message("user", prompt)
                    ))
                    .temperature(0.7)
                    .build();

            ChatCompletionResponse response = gptWebClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();

            if (response != null && !response.getChoices().isEmpty()) {
                return parseNewsFromResponse(response.getChoices().get(0).getMessage().getContent());
            }
            throw new RuntimeException("Failed to generate news from GPT API");
        } catch (Exception e) {
            log.error("Error generating news", e);
            throw new RuntimeException("Failed to generate news", e);
        }
    }

    private News parseNewsFromResponse(String content) {
        // 응답 형식:
        // - 제목: [뉴스 제목]
        // - 내용: [뉴스 내용]
        // - 영향을 받는 기업: [기업코드1, 기업코드2, ...]
        // - 감정점수: [-1.0 ~ 1.0]

        try {
            String[] lines = content.split("\n");
            News news = new News();
            news.setDate(LocalDate.now());  // 현재 날짜 설정

            for (String line : lines) {
                if (line.startsWith("- 제목: ")) {
                    news.setTitle(line.substring("- 제목: ".length()).trim());
                } else if (line.startsWith("- 내용: ")) {
                    news.setContent(line.substring("- 내용: ".length()).trim());
                } else if (line.startsWith("- 영향을 받는 기업: ")) {
                    String codes = line.substring("- 영향을 받는 기업: ".length()).trim();
                    List<Stock> affectedStocks = Arrays.stream(codes.split(", "))
                            .map(code -> {
                                Stock stock = new Stock();
                                stock.setCode(code);
                                return stock;
                            })
                            .collect(Collectors.toList());
                    news.setAffectedStocks(affectedStocks);
                } else if (line.startsWith("- 감정점수: ")) {
                    String score = line.substring("- 감정점수: ".length()).trim();
                    news.setSentimentScore(Double.parseDouble(score));
                }
            }

            // 뉴스 타입 설정 (이 부분은 호출하는 쪽에서 설정하는 것이 더 좋을 수 있습니다)
            if (news.getAffectedStocks().size() == 1) {
                news.setType(NewsType.COMPANY);
            } else if (news.getAffectedStocks().get(0).getCode().equals("ALL")) {
                news.setType(NewsType.MARKET);
            } else {
                news.setType(NewsType.INDUSTRY);
            }

            return news;
        } catch (Exception e) {
            log.error("Error parsing GPT response", e);
            throw new RuntimeException("Failed to parse news from GPT response", e);
        }
    }
}