package com.moneybricks.product.service;

import com.moneybricks.common.dto.PageRequestDTO;
import com.moneybricks.common.dto.PageResponseDTO;
import com.moneybricks.product.domain.ProductType;
import com.moneybricks.product.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ComparisonDepositService {
    PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO, ProductType productType);
}
