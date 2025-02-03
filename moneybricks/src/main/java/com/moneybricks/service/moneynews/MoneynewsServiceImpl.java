package com.moneybricks.service.moneynews;

import com.moneybricks.dto.MoneynewsDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoneynewsServiceImpl implements MoneynewsService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<MoneynewsDTO> getMoneyNews(String query, int page, int size) {
        int start = (page - 1) * size + 1;
        String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + query + "&start=" + start + "&display=" + size;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", "_nNvaWbywJUp0n6QIN5F");
        headers.set("X-Naver-Client-Secret", "Xc6MbYzrNG");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return parseResponse(response.getBody());
    }

    private List<MoneynewsDTO> parseResponse(String responseBody) {
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray items = jsonObject.getJSONArray("items");

        return items.toList().stream().map(item -> {
            JSONObject obj = new JSONObject((java.util.Map<?, ?>) item);
            MoneynewsDTO news = new MoneynewsDTO();
            news.setTitle(obj.getString("title"));
            news.setDescription(obj.getString("description"));
            news.setOriginallink(obj.getString("originallink"));
            news.setPubDate(obj.getString("pubDate"));
            return news;
        }).collect(Collectors.toList());
    }
}
