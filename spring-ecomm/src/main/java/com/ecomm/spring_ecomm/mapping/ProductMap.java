package com.ecomm.spring_ecomm.mapping;

import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductWithPaginationResponse;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMap {

    @Autowired
    ModelMapper modelMapper;

    public ProductWithPaginationResponse pageProductsToProductResponse(Page<Product> pageProducts) {

        List<Product> products = pageProducts.getContent();
        if (products.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_PRODUCTS_FOUND);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product ->modelMapper.map(product,ProductDTO.class))
                .toList();


        return  ProductWithPaginationResponse.builder()
                .products(productDTOS)
                .pageNumber(pageProducts.getNumber() + 1)
                .pageSize(pageProducts.getSize())
                .totalPages(pageProducts.getTotalPages())
                .totalElements(pageProducts.getTotalElements())
                .build();
    }
}
