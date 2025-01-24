package com.moneybricks.product.repository;

import com.moneybricks.product.domain.Product;
import com.moneybricks.product.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByProductType(ProductType productType, Pageable pageable);
}
