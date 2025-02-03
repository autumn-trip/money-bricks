package com.moneybricks.dictionary.service;

import com.moneybricks.dictionary.domain.Dictionary;
import com.moneybricks.dictionary.repository.DictionaryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@Log4j2
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository repository;

    public DictionaryServiceImpl(DictionaryRepository repository) {
        this.repository = repository;
    }

//    @Override
//    public List<Dictionary> getTermsByCode(Integer code) {
//        return repository.findByDictionaryCode(code);
//    }

    @Override
    public List<Dictionary> getTermsByCode(Integer code) {
        return List.of();
    }

    @Override
    public Page<Dictionary> searchTerms(String keyword, Pageable pageable) {
        return repository.findByDictionaryTermsContaining(keyword, pageable);
    }

    @Override
    public List<Dictionary> getAllTerms() {
        return repository.findAll();
    }

    @Override
    public Page<Dictionary> getAllTerms(String sortBy, Pageable pageable) {
        return repository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy)));
    }

//    @Override
//    public Page<Dictionary> getTermsByCode(Integer code, Pageable pageable) {
//        return repository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() ));
//    }

    @Override
    public Page<Dictionary> searchTerms(String sortBy, String keyword, Pageable pageable) {
        return repository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy)));
    }

    @Override
    public Page<Dictionary> getAllTerms(Pageable pageable) {
        return repository.findAll(pageable);
    }
    @Override
    public Page<Dictionary> getTermsByCode(Integer dictionaryCode, Pageable pageable) {
        return repository.findByDictionaryCode(dictionaryCode, pageable);
    }

    //0126
    @Override
    public Page<Dictionary> searchTermsByCode(String keyword, Integer code, Pageable pageable) {
        return repository.findByDictionaryTermsContainingAndDictionaryCode(keyword, code, pageable);
    }


    //    @Override
//    public Page<Dictionary> getPaginatedTerms(Pageable pageable) {
//        return repository.findAll(pageable);
//    }
//
//    @Override
//    public Page<Dictionary> searchPaginatedTerms(String keyword, Pageable pageable) {
//        return repository.findByDictionaryTermsContaining(keyword, pageable);



}
