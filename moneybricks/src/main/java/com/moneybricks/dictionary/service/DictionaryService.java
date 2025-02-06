package com.moneybricks.dictionary.service;

import com.moneybricks.dictionary.domain.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface DictionaryService {
    Page<Dictionary> getTermsByCode(Integer code, Pageable pageable);
    Page<Dictionary> searchTerms(String keyword, Pageable pageable);
    Page<Dictionary> getAllTerms(Pageable pageable);
    Page<Dictionary> searchTermsByCode(String keyword, Integer code, Pageable pageable); // 새로운 메서드
    List<Dictionary> getRandomTerms(); // 메인 페이지
}
