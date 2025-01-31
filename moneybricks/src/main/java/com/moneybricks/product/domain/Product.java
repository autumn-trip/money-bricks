package com.moneybricks.product.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    private String finPrdtCd; // 상품 코드 (PK)
    private String dclsMonth;
    private String finCoNo;
    private String korCoNm; // 은행명
    private String finPrdtNm; // 상품명
    private String joinWay;
    private String mtrtInt; // 금리
    private String spclCnd; // 우대 이율
    private String joinDeny;
    private String joinMember;
    private String etcNote;
    private int maxLimit;
    private String dclsStrtDay;
    private String dclsEndDay;
    private String finCoSubmDay;

    // option 데이터
    private double intrRate; // 기본 금리
    private double intrRate2; // 최고 금리

    @Enumerated(EnumType.STRING)
    private ProductType productType; // 예금, 적금 타입으로 분류
}
