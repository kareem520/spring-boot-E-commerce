package com.ecomm.spring_ecomm.controllers;

import com.ecomm.spring_ecomm.DTOS.category.*;
import com.ecomm.spring_ecomm.configurations.AppConstants;
import com.ecomm.spring_ecomm.models.Category;
import com.ecomm.spring_ecomm.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {


    @Autowired
    CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryWithPaginationResponse> getAll(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        return new ResponseEntity(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }
    @GetMapping("/public/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String id){
        return new ResponseEntity<>(categoryService.getCategoryById(id),HttpStatus.OK);
    }
    @PostMapping("/admin/add-category")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CreateCategoryRequest category){
        CategoryDTO newCategory = categoryService.addCategory(category);

        return new ResponseEntity<>(newCategory,HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void>deleteCategory(String categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryUpdateResponse> updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryUpdateNameRequest createCategoryRequest){
      CategoryUpdateResponse response =  categoryService.updateCategoryName(categoryId,createCategoryRequest);

      return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
