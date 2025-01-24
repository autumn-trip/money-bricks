package com.moneybricks.product.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductType {
    FIXED, //예금
    SAVINGS; // 적금
}
