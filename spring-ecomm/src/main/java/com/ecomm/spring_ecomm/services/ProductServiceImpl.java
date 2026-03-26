package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductWithPaginationResponse;
import com.ecomm.spring_ecomm.Repositories.CategoryRepository;
import com.ecomm.spring_ecomm.Repositories.ProductRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.helper.Calculation;
import com.ecomm.spring_ecomm.mapping.ProductMap;
import com.ecomm.spring_ecomm.models.Category;
import com.ecomm.spring_ecomm.models.Product;
import com.ecomm.spring_ecomm.models.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    AuthUtil authUtil;


    @Override
    public ProductWithPaginationResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product>pageProducts =  productRepository.findAllByIsActiveTrue(pageDetails);


        return
                productMap.pageProductsToProductResponse(pageProducts);
    }

    @Override
    public ProductWithPaginationResponse searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Category",categoryId));


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByIsActiveTrueAndCategory_IdOrderByPriceAsc(categoryId, pageDetails);


        return
                productMap.pageProductsToProductResponse(pageProducts);

    }


    @Override
    public ProductWithPaginationResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByIsActiveTrueAndNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        return
                productMap.pageProductsToProductResponse(pageProducts);

    }

    @Override
    public ProductWithPaginationResponse getMyProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
       User seller = authUtil.loggedInUser();
       if (seller == null) {
           throw new BusinessException(ErrorCode.SELLER_MUST_BE_LOGGED_IN);
       }


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByIsActiveTrueAndSeller_Id(seller.getId(), pageDetails);

        return productMap.pageProductsToProductResponse(pageProducts);

    }


    @Override
    @Transactional
    public ProductDTO addProductToYourProducts(String categoryId, CreateProductRequest productRequest) {

        User seller = authUtil.loggedInUser();
        if (seller == null) {
            throw new BusinessException(ErrorCode.SELLER_MUST_BE_LOGGED_IN);
        }


        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND
                        ,"Category",categoryId));

        String productBelongsToAnotherCategory = productRepository.productBelongsToAnotherCategory(categoryId, productRequest.getName())
                .orElse(null);

        if (productBelongsToAnotherCategory!=null) {
            throw new BusinessException(ErrorCode.PRODUCT_BELONGS_TO_ANOTHER_CATEGORY);
        }

        List<Product> products = seller.getProducts();

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
            newProduct.setSeller(seller);
            productRepository.save(newProduct);
            ProductDTO newProductDTO = modelMapper.map(newProduct, ProductDTO.class);
            newProductDTO.setSpecialPrice(Calculation.calculateSpecialPrice(newProduct.getPrice()
                    ,newProduct.getDiscountPercentage()));
            newProduct.setIsActive(true);
            return newProductDTO;
        }else
        {
            throw new BusinessException(ErrorCode.PRODUCT_ALREADY_EXISTS_IN_YOUR_PRODUCTS);
        }
    }

    @Override
    @Transactional
    public void takeQuantityFromProduct(String productId, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        if (product.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.NO_ENOUGH_QUANTITY_FOUND,product.getName());
        }
        product.setQuantity(product.getQuantity() - quantity);
    }

    @Override
    public void deactivateProduct( String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        product.setIsActive(false);

    }


}
