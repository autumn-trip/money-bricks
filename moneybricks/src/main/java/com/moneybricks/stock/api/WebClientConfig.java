package com.moneybricks.stock.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${kis.api.url}")
    private String kisApiUrl;

    @Value("${kis.api.appkey}")
    private String kisAppKey;

    @Value("${kis.api.appsecret}")
    private String kisAppSecret;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    public WebClient kisWebClient() {
        return WebClient.builder()
                .baseUrl(kisApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient gptWebClient() {
        return WebClient.builder()
                .baseUrl(openaiApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .build();
    }
}