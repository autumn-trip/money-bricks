package com.moneybricks.stock.repository;

import com.moneybricks.stock.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByDateOrderByTypeDesc(LocalDate date);
}
