package com.moneybricks.service;

import com.moneybricks.product.domain.Product;
import com.moneybricks.product.domain.ProductType;
import com.moneybricks.product.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testFetchAndSaveProduct() {
        // 예금 API URL
        String apiUrl = "https://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json?auth=e31c0c1507323fe881aee0c48b23a867&topFinGrpNo=020000&pageNo=1";

        // Service 호출
        List<Product> savedProducts = productService.fetchAndSaveProducts(apiUrl, ProductType.FIXED);

        assertThat(savedProducts).isNotEmpty();
        assertThat(savedProducts.get(0).getFinPrdtCd()).isNotNull();
        assertThat(savedProducts.get(0).getProductType()).isEqualTo(ProductType.FIXED);
    }

    @Test
    public void testFetchAndSaveSavingProduct() {
        // 적금 API URL
        String apiUrl = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth=e31c0c1507323fe881aee0c48b23a867&topFinGrpNo=020000&pageNo=1";

        // Service 호출
        List<Product> savedProducts = productService.fetchAndSaveProducts(apiUrl, ProductType.SAVINGS);

        assertThat(savedProducts).isNotEmpty();
        assertThat(savedProducts.get(0).getFinPrdtCd()).isNotNull();
        assertThat(savedProducts.get(0).getProductType()).isEqualTo(ProductType.SAVINGS);
    }

    @Test
    public void testFetchAndSaveAllProducts() {
        productService.fetchAndSaveAllProducts();
        log.info("데이터 저장 완료");
    }
}
