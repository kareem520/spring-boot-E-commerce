package com.ecomm.spring_ecomm.DTOS.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotBlank(message = "Category name must be not empty")
    @Size(min = 2, max = 50, message = "Category name must be between {min} and {max} characters")
    private String name;
}
