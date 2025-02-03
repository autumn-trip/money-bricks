package com.moneybricks.dictionary.controller;

import com.moneybricks.dictionary.domain.Dictionary;
import com.moneybricks.dictionary.service.DictionaryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    // 1. 전체 용어 조회 (페이징 + 정렬)
    @GetMapping("/all")
    public ResponseEntity<Page<Dictionary>> getAllTerms(Pageable pageable) {
        Page<Dictionary> terms = dictionaryService.getAllTerms(pageable);
        return ResponseEntity.ok(terms);
    }
//0123
//    @GetMapping("/all")
//    public Page<Dictionary> getAllTerms(@RequestParam(defaultValue = "dictionaryTerms") String sortBy, Pageable pageable) {
//        return dictionaryService.getAllTerms(sortBy, pageable);
//    }

    // 2. 코드별 조회 (예외 처리 포함)
    @GetMapping("/code")
    public ResponseEntity<Page<Dictionary>> getTermsByCode(
            @RequestParam Integer code,
            Pageable pageable) {
        Page<Dictionary> terms = dictionaryService.getTermsByCode(code, pageable);
        return ResponseEntity.ok(terms);
    }

//    @GetMapping("/code")public ResponseEntity<Page<Dictionary>> getTermsByCode(@RequestParam(required = false, defaultValue = "1") Integer code , Pageable pageable) {
//        if (code == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        Page<Dictionary> terms = dictionaryService.getTermsByCode(code, pageable);
////        List<Dictionary> terms = dictionaryService.getTermsByCode(code);
//        if (terms.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(terms);
//
//
//    }

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
    }}




//    @GetMapping("/search")
//    public ResponseEntity<List<Dictionary>> searchTerms(@RequestParam String keyword) {
//        List<Dictionary> terms = dictionaryService.searchTerms(keyword);
//        if (terms.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//        }
//        return ResponseEntity.ok(terms);
//    }
//}
