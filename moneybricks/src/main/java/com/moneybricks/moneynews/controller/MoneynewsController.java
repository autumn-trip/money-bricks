package com.moneybricks.moneynews.controller;

import com.moneybricks.moneynews.dto.MoneynewsDTO;
import com.moneybricks.moneynews.service.MoneynewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moneynews")
@RequiredArgsConstructor
public class MoneynewsController {

    private final MoneynewsService newsService;

    @GetMapping
    public ResponseEntity<List<MoneynewsDTO>> getMoneyNews(@RequestParam String query,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<MoneynewsDTO> newsList = newsService.getMoneyNews(query, page, size);
        return ResponseEntity.ok(newsList);
    }
}
