package com.moneybricks.product.service;

import com.moneybricks.product.domain.Product;
import com.moneybricks.product.domain.ProductType;
import com.moneybricks.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    // option 데이터 API URL (예금)
    private static final String API_URL = "https://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json?auth=e31c0c1507323fe881aee0c48b23a867&topFinGrpNo=020000&pageNo=1";

    // 적금
    // private static final String APU_URL = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth=e31c0c1507323fe881aee0c48b23a867&topFinGrpNo=020000&pageNo=1"

    @Transactional
    public List<Product> fetchAndSaveProducts(String apiUrl, ProductType productType) {
        // API 호출
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        // JSON 파싱
        List<Map<String, Object>> productDataList = (List<Map<String, Object>>) ((Map<String, Object>) response.get("result")).get("baseList");

        // 엔티티 변환 및 저장
        List<Product> deposits = new ArrayList<>();
        for (Map<String, Object> data : productDataList) {
            Product deposit = new Product();
            deposit.setDclsMonth((String) data.get("dcls_month"));
            deposit.setFinCoNo((String) data.get("fin_co_no"));
            deposit.setFinPrdtCd((String) data.get("fin_prdt_cd"));
            deposit.setKorCoNm((String) data.get("kor_co_nm"));
            deposit.setFinPrdtNm((String) data.get("fin_prdt_nm"));
            deposit.setJoinWay((String) data.get("join_way"));
            deposit.setMtrtInt((String) data.get("mtrt_int"));
            deposit.setSpclCnd((String) data.get("spcl_cnd"));
            deposit.setJoinDeny((String) data.get("join_deny"));
            deposit.setJoinMember((String) data.get("join_member"));
            deposit.setEtcNote((String) data.get("etc_note"));
//            deposit.setMaxLimit(Integer.parseInt(String.valueOf((Integer) data.get("max_limit"))));
            deposit.setDclsStrtDay((String) data.get("dcls_strt_day"));
            deposit.setDclsEndDay((String) data.get("dcls_end_day"));
            deposit.setFinCoSubmDay((String) data.get("fin_co_subm_day"));
            deposit.setProductType(productType); // 예금/적금 구분

            // spcl_cnd 길이 제한
            String spclCnd = (String) data.get("spcl_cnd");
            if (spclCnd != null && spclCnd.length() > 255) {
                spclCnd = spclCnd.substring(0, 255);
            }
            deposit.setSpclCnd(spclCnd);

            deposits.add(deposit);
        }
        return productRepository.saveAll(deposits);
    }

    @Transactional
    public void fetchAndSaveAllProducts() {
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Map<String, Object>> optionList = (List<Map<String, Object>>) result.get("optionList");

        for (Map<String, Object> option : optionList) {
            String finPrdtCd = (String) option.get("fin_prdt_cd");
            if (finPrdtCd == null || finPrdtCd.isEmpty()) {
                throw new IllegalArgumentException("finPrdtCd cannot be null or empty");
            }

            // DB에서 해당 finPrdtCd가 있는지 확인
            Product existingProduct = productRepository.findByFinPrdtCd(finPrdtCd).orElse(null);

            if (existingProduct != null) {
                // 기존 데이터 업데이트
                updateProduct(existingProduct, option);
            } else {
                // 새 데이터 생성
                Product newProduct = createProduct(option);
                productRepository.save(newProduct);
            }
        }
    }

    private void updateProduct(Product product, Map<String, Object> option) {
        // 기본 금리
        Number intrRate = (Number) option.get("intr_rate");
        product.setIntrRate(intrRate != null ? intrRate.doubleValue() : 0.0);

        // 최고 금리
        Number intrRate2 = (Number) option.get("intr_rate2");
        product.setIntrRate2(intrRate2 != null ? intrRate2.doubleValue() : 0.0);
    }

    private Product createProduct(Map<String, Object> option) {
        Product product = new Product();
        product.setFinPrdtCd((String) option.get("fin_prdt_cd"));
        product.setFinCoNo((String) option.get("fin_co_no"));
        product.setDclsMonth((String) option.get("dcls_month"));

        // 기본 금리
        Number intrRate = (Number) option.get("intr_rate");
        product.setIntrRate(intrRate != null ? intrRate.doubleValue() : 0.0);

        // 최고 금리
        Number intrRate2 = (Number) option.get("intr_rate2");
        product.setIntrRate2(intrRate2 != null ? intrRate2.doubleValue() : 0.0);

        // 추가적인 필드 초기화가 필요한 경우 여기에 추가

        return product;
    }
}
