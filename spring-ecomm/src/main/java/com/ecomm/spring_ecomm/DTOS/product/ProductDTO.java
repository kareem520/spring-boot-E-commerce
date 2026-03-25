package com.ecomm.spring_ecomm.DTOS.product;

import lombok.Data;

@Data
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private double discountPercentage;
    private double specialPrice;
    private String categoryName;
    private  Integer quantity;
    private String sellerEmail;
}
