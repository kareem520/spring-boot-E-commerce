package com.ecomm.spring_ecomm.DTOS.category;

import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private String id;
    private String name;
    List<ProductDTO> products;
}
