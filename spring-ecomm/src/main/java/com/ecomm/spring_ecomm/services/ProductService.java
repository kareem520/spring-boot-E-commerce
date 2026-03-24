package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductResponse;
import com.ecomm.spring_ecomm.models.Product;

import java.util.List;

public interface ProductService {

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO createProduct(String categoryId, CreateProductRequest productRequest);
    void takeQuantityFromProduct(String categoryId, String productId, Integer quantity);
}
