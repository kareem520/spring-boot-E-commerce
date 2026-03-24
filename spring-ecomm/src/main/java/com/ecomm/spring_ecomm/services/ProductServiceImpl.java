package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductResponse;
import com.ecomm.spring_ecomm.Repositories.CategoryRepository;
import com.ecomm.spring_ecomm.Repositories.ProductRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.helper.Calculation;
import com.ecomm.spring_ecomm.mapping.ProductMap;
import com.ecomm.spring_ecomm.models.Category;
import com.ecomm.spring_ecomm.models.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ProductMap productMap;


    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product>pageProducts =  productRepository.findAll(pageDetails);


        return
                productMap.pageProductsToProductResponse(pageProducts);
    }

    @Override
    public ProductResponse searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Category",categoryId));


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategory_IdOrderByPriceAsc(categoryId, pageDetails);


        return
                productMap.pageProductsToProductResponse(pageProducts);

    }


    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        return
                productMap.pageProductsToProductResponse(pageProducts);

    }


    @Override
    public ProductDTO createProduct(String categoryId, CreateProductRequest productRequest) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND
                        ,"Category",categoryId));

        List<Product> products = category.getProducts();

        boolean isPresent = false;

        for  (Product p : products) {
            if(p.getName().equals(productRequest.getName())) {
                isPresent = true;
                break;
            }
        }

        if(!isPresent) {
            Product newProduct = modelMapper.map(productRequest, Product.class);
            newProduct.setCategory(category);
            productRepository.save(newProduct);
            ProductDTO newProductDTO = modelMapper.map(newProduct, ProductDTO.class);
            newProductDTO.setSpecialPrice(Calculation.calculateSpecialPrice(newProduct.price
                    ,newProduct.discountPercentage));
            return newProductDTO;
        }else
        {
            throw new BusinessException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public void takeQuantityFromProduct(String categoryId, String productId, Integer quantity) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Category",categoryId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        if (product.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.NO_ENOUGH_QUANTITY_FOUND,product.getName());
        }
        product.setQuantity(product.getQuantity() - quantity);
    }

}
