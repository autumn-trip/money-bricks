package com.moneybricks.dictionary.controller;

import com.moneybricks.dictionary.domain.Dictionary;
import com.moneybricks.dictionary.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    // 1. 전체 용어 조회 (페이징 + 정렬)
    @GetMapping("/all")
    public ResponseEntity<Page<Dictionary>> getAllTerms(Pageable pageable) {
        Page<Dictionary> terms = dictionaryService.getAllTerms(pageable);
        return ResponseEntity.ok(terms);
    }

    // 2. 코드별 조회 (예외 처리 포함)
    @GetMapping("/code")
    public ResponseEntity<Page<Dictionary>> getTermsByCode(
            @RequestParam Integer code,
            Pageable pageable) {
        Page<Dictionary> terms = dictionaryService.getTermsByCode(code, pageable);
        return ResponseEntity.ok(terms);
    }

    // 3. 키워드 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Dictionary>> searchTerms(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer code, // 기본값으로 null 처리
            Pageable pageable) {

        Page<Dictionary> terms;
        if (code != null && code > 0) { // 유효한 code만 처리
            terms = dictionaryService.searchTermsByCode(keyword, code, pageable);
        } else {
            terms = dictionaryService.searchTerms(keyword, pageable);
        }

        if (terms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(terms);
    }

    // 4. 메인 페이지
    @GetMapping("/random")
    public ResponseEntity<List<Dictionary>> getRandomTerms() {
        return ResponseEntity.ok(dictionaryService.getRandomTerms());
    }
}