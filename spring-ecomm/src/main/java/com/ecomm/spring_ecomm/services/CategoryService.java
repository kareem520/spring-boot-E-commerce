package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.category.*;

public interface CategoryService {

    CategoryWithPaginationResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO getCategoryById(String id);
    CategoryDTO addCategory(CreateCategoryRequest category);
    void deleteCategory(String id);
    CategoryUpdateResponse updateCategoryName(String id, CategoryUpdateNameRequest categoryUpdateNameRequest);
}
