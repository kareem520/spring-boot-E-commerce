package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductWithPaginationResponse;
import com.ecomm.spring_ecomm.configurations.AppConstants;
import com.ecomm.spring_ecomm.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    AuthUtil  authUtil;


    //PUBLIC
    @GetMapping("/public/products")
    public ResponseEntity<ProductWithPaginationResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }

   @GetMapping("/public/categories/{categoryId}/products")
   public ResponseEntity<ProductWithPaginationResponse> getProductsByCategoryId(@PathVariable String categoryId,
                                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
       ProductWithPaginationResponse productWithPaginationResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
       return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
   }
   @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductWithPaginationResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                              @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                              @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                              @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductWithPaginationResponse productWithPaginationResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
   }



   //SELER
    @GetMapping("/seller/products")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ProductWithPaginationResponse> getMyProducts(       @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                              @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                              @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductWithPaginationResponse productWithPaginationResponse = productService.getMyProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
    }
    @PostMapping("/seller/categories/{CategoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable("CategoryId") String categoryId
            , @Valid @RequestBody CreateProductRequest product) {
        return new ResponseEntity<>(productService.addProductToYourProducts(categoryId,product),HttpStatus.CREATED);

    }
    @DeleteMapping("seller/{productId}")
    @PreAuthorize("hasAuthority('ROLE_SELLER') and @productServiceSecurity.isProductOwner(#productId)")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.ok().build();
    }

}
