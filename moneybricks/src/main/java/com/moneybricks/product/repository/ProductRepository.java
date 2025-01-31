package com.moneybricks.product.repository;

import com.moneybricks.product.domain.Product;
import com.moneybricks.product.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByProductType(@Param("productType") ProductType productType, Pageable pageable);

    // API 데이터 받아오기전 기본키 체크를 위한 로직
    Optional<Product> findByFinPrdtCd(String finPrdtCd);
}
