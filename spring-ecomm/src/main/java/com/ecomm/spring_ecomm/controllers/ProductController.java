package com.ecomm.spring_ecomm.controllers;


import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.product.CreateProductRequest;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.DTOS.product.ProductWithPaginationResponse;
import com.ecomm.spring_ecomm.configurations.AppConstants;
import com.ecomm.spring_ecomm.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "products", description = "product API")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    AuthUtil  authUtil;


    // PUBLIC
    @Operation(summary = "Get a paginated list of all products with optional sorting")
    @GetMapping("/public/products")
    public ResponseEntity<ProductWithPaginationResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @Operation(summary = "Get a paginated list of products by category ID with optional sorting")
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductWithPaginationResponse> getProductsByCategoryId(@PathVariable String categoryId,
                                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductWithPaginationResponse productWithPaginationResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
    }

    @Operation(summary = "Search for products by keyword with pagination and optional sorting")
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductWithPaginationResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                              @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                              @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                              @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductWithPaginationResponse productWithPaginationResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
    }

    // SELLER
    @Operation(summary = "Get a paginated list of the seller's products with optional sorting")
    @GetMapping("/seller/products")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ProductWithPaginationResponse> getMyProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductWithPaginationResponse productWithPaginationResponse = productService.getMyProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productWithPaginationResponse, HttpStatus.OK);
    }

    @Operation(summary = "Add a new product under a specific category (seller only)")
    @PostMapping("/seller/categories/{CategoryId}/product")
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable("CategoryId") String categoryId,
            @Valid @RequestBody CreateProductRequest product) {
        return new ResponseEntity<>(productService.addProductToYourProducts(categoryId, product), HttpStatus.CREATED);
    }

    @Operation(summary = "Deactivate (delete) a product owned by the seller")
    @DeleteMapping("/seller/{productId}")
    @PreAuthorize("hasAuthority('ROLE_SELLER') and @productServiceSecurity.isProductOwner(#productId)")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Increase the quantity of a seller's product")
    @PutMapping("/seller/{productId}/quantity/{quantity}")
    @PreAuthorize("hasAuthority('ROLE_SELLER') and @productServiceSecurity.isProductOwner(#productId)")
    public ResponseEntity<ProductDTO> addQuantityForProduct(@PathVariable String productId, @PathVariable Integer quantity) {
        return new ResponseEntity<>(productService.addQuantityToYourProduct(productId, quantity), HttpStatus.OK);
    }

    @Operation(summary = "Update the discount percentage of a seller's product")
    @PutMapping("/seller/{productId}/discount/{discountPercentage}")
    @PreAuthorize("hasAuthority('ROLE_SELLER') and @productServiceSecurity.isProductOwner(#productId)")
    public ResponseEntity<ProductDTO> addQuantityForProduct(@PathVariable String productId, @PathVariable Double discountPercentage) {
        return new ResponseEntity<>(productService.updateProductDiscount(productId, discountPercentage), HttpStatus.OK);
    }
}
