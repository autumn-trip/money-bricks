package com.moneybricks.product.controller;

import com.moneybricks.common.dto.PageRequestDTO;
import com.moneybricks.common.dto.PageResponseDTO;
import com.moneybricks.product.domain.ProductType;
import com.moneybricks.product.dto.ProductDTO;
import com.moneybricks.product.service.ComparisonDepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/product")
public class ComparisonDepositController {

    private final ComparisonDepositService comparisonDepositService;

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO,
                                            @RequestParam(required = false) ProductType productType) {

        log.info("PageRequestDTO : {}", pageRequestDTO);
        log.info("ProductType : {}", productType);

        return comparisonDepositService.list(pageRequestDTO, productType);
    }
}
