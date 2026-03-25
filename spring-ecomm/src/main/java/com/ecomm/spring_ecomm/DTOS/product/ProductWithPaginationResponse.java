package com.ecomm.spring_ecomm.DTOS.product;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithPaginationResponse {
    List<ProductDTO> products = new ArrayList<>();
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
    private String sellerEmail;

}
