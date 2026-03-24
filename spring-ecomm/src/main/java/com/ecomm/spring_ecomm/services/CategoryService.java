package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.category.CategoryDTO;
import com.ecomm.spring_ecomm.DTOS.category.CategoryResponse;
import com.ecomm.spring_ecomm.DTOS.category.CreateCategoryRequest;
import com.ecomm.spring_ecomm.models.Category;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO getCategoryById(String id);
    CategoryDTO addCategory(CreateCategoryRequest category);

}
