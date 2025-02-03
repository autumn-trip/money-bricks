package com.moneybricks.repository.dictionary;

import com.moneybricks.domain.dictionary.Dictionary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
//   Page<Dictionary> findByDictionaryCode(Integer code, Pageable pageable); // code로 조회
//   List<Dictionary> findByDictionaryTermsContaining(String keyword); // 검색어 포함 조회

    // 전체 데이터를 페이지네이션과 함께 가져오기
    Page<Dictionary> findAll(Pageable pageable);

    // 키워드를 기반으로 검색한 데이터를 페이지네이션과 함께 가져오기
    Page<Dictionary> findByDictionaryTermsContaining(String dictionaryTerms, Pageable pageable);

    Page<Dictionary> findByDictionaryCode(Integer dictionaryCode, Pageable pageable);

    Page<Dictionary> findByDictionaryTermsContainingAndDictionaryCode(String keyword, Integer code, Pageable pageable);
}





