package com.moneybricks.dictionary.repository;

import com.moneybricks.dictionary.domain.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    // 전체 데이터를 페이지네이션과 함께 가져오기
    Page<Dictionary> findAll(Pageable pageable);

    // 키워드를 기반으로 검색한 데이터를 페이지네이션과 함께 가져오기
    Page<Dictionary> findByDictionaryTermsContaining(String dictionaryTerms, Pageable pageable);
    Page<Dictionary> findByDictionaryCode(Integer dictionaryCode, Pageable pageable);
    Page<Dictionary> findByDictionaryTermsContainingAndDictionaryCode(String keyword, Integer code, Pageable pageable);

    // 메인 페이지 활용 로직
    @Query(value = "select * from dictionary order by rand() limit 5", nativeQuery = true)
    List<Dictionary> findRandomTerms();
}





