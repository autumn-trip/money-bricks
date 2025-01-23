package com.moneybricks.product.service;

import com.moneybricks.common.dto.PageRequestDTO;
import com.moneybricks.common.dto.PageResponseDTO;
import com.moneybricks.product.domain.Product;
import com.moneybricks.product.dto.ProductDTO;
import com.moneybricks.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ComparisonDepositServiceImpl implements ComparisonDepositService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable =
                PageRequest.of(
                        pageRequestDTO.getPage() - 1 ,  // 1페이지가 0이므로 주의
                        pageRequestDTO.getSize(),
                        Sort.by("finPrdtCd").descending());

        Page<Product> result = productRepository.findAll(pageable);

        List<ProductDTO> dtoList = result.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        return responseDTO;
    }
}
