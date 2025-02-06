package com.moneybricks.dictionary.service;

import com.moneybricks.dictionary.domain.Dictionary;
import com.moneybricks.dictionary.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository repository;

    @Override
    public Page<Dictionary> searchTerms(String keyword, Pageable pageable) {
        return repository.findByDictionaryTermsContaining(keyword, pageable);
    }

    @Override
    public Page<Dictionary> getAllTerms(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Dictionary> getTermsByCode(Integer dictionaryCode, Pageable pageable) {
        return repository.findByDictionaryCode(dictionaryCode, pageable);
    }

    @Override
    public Page<Dictionary> searchTermsByCode(String keyword, Integer code, Pageable pageable) {
        return repository.findByDictionaryTermsContainingAndDictionaryCode(keyword, code, pageable);
    }

    // 메인 페이지
    @Override
    public List<Dictionary> getRandomTerms() {
        return repository.findRandomTerms();
    }
}
