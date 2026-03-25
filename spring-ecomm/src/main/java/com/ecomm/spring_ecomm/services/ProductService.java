package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductWithPaginationResponse;

public interface ProductService {

    ProductWithPaginationResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductWithPaginationResponse searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductWithPaginationResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductWithPaginationResponse getMyProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO addProductToYourProducts(String categoryId, CreateProductRequest productRequest);

    void takeQuantityFromProduct(String productId, Integer quantity);

    void deactivateProduct(String productId);
}
