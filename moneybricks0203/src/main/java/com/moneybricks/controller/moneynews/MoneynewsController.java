package com.moneybricks.controller.moneynews;

import com.moneybricks.dto.MoneynewsDTO;
import com.moneybricks.service.moneynews.MoneynewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moneynews")
public class MoneynewsController {

    private final MoneynewsService newsService;

    public MoneynewsController(MoneynewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<MoneynewsDTO>> getMoneyNews(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<MoneynewsDTO> newsList = newsService.getMoneyNews(query, page, size);
        return ResponseEntity.ok(newsList);
    }
}
