package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.DTOS.category.*;
import com.ecomm.spring_ecomm.Repositories.CategoryRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.mapping.CategoryMap;
import com.ecomm.spring_ecomm.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryMap categoryMap;

    @Override
    public CategoryWithPaginationResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> pageCategories =  categoryRepository.findAll(pageDetails);


       return categoryMap.
               pageCategoriesToCategoryResponse(pageCategories);
    }
    @Override
    public CategoryDTO getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND
                        ,"Category",id));
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO addCategory(CreateCategoryRequest categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);

        Category isExist = categoryRepository.findByName(category.getName());
        if (isExist!=null) {
            throw new BusinessException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Category",id));

        if (!category.getProducts().isEmpty()) {
            throw new BusinessException(ErrorCode.CATEGORY_HAVE_PRODUCTS);
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryUpdateResponse updateCategoryName(String id, CategoryUpdateNameRequest createCategoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Category",id));

        Category isExist = categoryRepository.findByName(category.getName());
        if (isExist!=null) {
            throw new BusinessException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        if (!category.getProducts().isEmpty()) {
            throw new BusinessException(ErrorCode.CATEGORY_HAVE_PRODUCTS);
        }
        category.setName(createCategoryRequest.getName());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory,CategoryUpdateResponse.class);
    }
}
