package com.moneybricks.service.dictionary;

import com.moneybricks.domain.dictionary.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Transactional
@Service
public interface DictionaryService {

//    List<Dictionary> getAllTerms();

//    Page<Dictionary> getAllTerms(String sortBy, Pageable pageable);

    Page<Dictionary> getTermsByCode(Integer code, Pageable pageable);

//    Page<Dictionary> searchTerms(String sortBy, String keyword, Pageable pageable);
////
//    List<Dictionary> getTermsByCode(Integer code);

    Page<Dictionary> searchTerms(String keyword, Pageable pageable);

    Page<Dictionary> getAllTerms(Pageable pageable);

    //0126
    Page<Dictionary> searchTermsByCode(String keyword, Integer code, Pageable pageable); // 새로운 메서드



}
