package com.ecomm.spring_ecomm.mapping;

import com.ecomm.spring_ecomm.DTOS.category.CategoryDTO;
import com.ecomm.spring_ecomm.DTOS.category.CategoryResponse;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMap {

    @Autowired
    ModelMapper modelMapper;

    public CategoryResponse pageCategoriesToCategoryResponse(Page<Category> pageDetails){

        List<Category> categories = pageDetails.getContent();

        if (categories.isEmpty()){
            throw new BusinessException(ErrorCode.NO_Categories_FOUND);
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category ->
                    modelMapper.map(category,CategoryDTO.class))
                .toList();

        return  CategoryResponse.builder()
                .categories(categoryDTOS)
                .pageNumber(pageDetails.getNumber()+1)
                .pageSize(pageDetails.getSize())
                .totalPages(pageDetails.getTotalPages())
                .totalElements(pageDetails.getTotalElements())
                .build();

    }
}
