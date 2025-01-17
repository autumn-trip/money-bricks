package com.moneybricks.stock.api;

import com.moneybricks.stock.dto.KisApiResponse;
import com.moneybricks.stock.dto.KisTokenRequest;
import com.moneybricks.stock.dto.KisTokenResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class KisApiClient {
    private final WebClient kisWebClient;

    @Value("${kis.api.appkey}")
    private String appKey;

    @Value("${kis.api.appsecret}")
    private String appSecret;

    private String accessToken;
    private LocalDateTime tokenExpireTime;

    @PostConstruct
    private void initialize() {
        updateAccessToken();
    }

    public double getCurrentPrice(String stockCode) {
        checkAndRefreshToken();

        try {
            KisApiResponse response = kisWebClient.get()
                    .uri("/uapi/domestic-stock/v1/quotations/inquire-price" +
                            "?FID_COND_MRKT_DIV_CODE=J" +
                            "&FID_INPUT_ISCD=" + stockCode)
                    .header("authorization", "Bearer " + accessToken)
                    .header("appkey", appKey)
                    .header("appsecret", appSecret)
                    .retrieve()
                    .bodyToMono(KisApiResponse.class)
                    .block();

            if (response != null && response.getOutput() != null) {
                return Double.parseDouble(response.getOutput().get("stck_prpr"));
            }
            throw new RuntimeException("Failed to get stock price from KIS API");
        } catch (Exception e) {
            log.error("Error getting stock price for code: " + stockCode, e);
            throw new RuntimeException("Failed to get stock price", e);
        }
    }

    private void checkAndRefreshToken() {
        if (accessToken == null || LocalDateTime.now().isAfter(tokenExpireTime)) {
            updateAccessToken();
        }
    }

    private void updateAccessToken() {
        try {
            KisTokenResponse tokenResponse = kisWebClient.post()
                    .uri("/oauth2/tokenP")
                    .header("appkey", appKey)
                    .header("appsecret", appSecret)
                    .bodyValue(new KisTokenRequest("client_credentials"))
                    .retrieve()
                    .bodyToMono(KisTokenResponse.class)
                    .block();

            if (tokenResponse != null && tokenResponse.getAccess_token() != null) {
                this.accessToken = tokenResponse.getAccess_token();
                this.tokenExpireTime = LocalDateTime.now().plusSeconds(tokenResponse.getExpires_in());
            }
        } catch (Exception e) {
            log.error("Error updating KIS API access token", e);
            throw new RuntimeException("Failed to update access token", e);
        }
    }
}