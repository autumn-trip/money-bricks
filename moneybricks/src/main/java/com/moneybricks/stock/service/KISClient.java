package com.moneybricks.stock.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class KISClient {
    private static final String BASE_URL = "https://openapi.koreainvestment.com:9443";
    private final RestTemplate restTemplate;

    public KISClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken(String appkey, String appSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appkey);
        body.put("appsecret", appSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                BASE_URL + "/oauth2/tokenP",
                request,
                TokenResponse.class
        );
        return response.getBody().getAccess_token();
    }
}
