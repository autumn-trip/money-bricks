package com.moneybricks.product.dto;

import com.moneybricks.product.domain.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String finPrdtCd; // 상품 코드
    private String korCoNm; // 은행명
    private String finPrdtNm; // 상품명
    private String mtrtInt; // 금리
    private String spclCnd; // 우대 이율
    private ProductType productType; // 예금 적금 구분
    private double intrRate; // 기본 금리
    private double intrRate2; // 최고 금리
}
