package com.ecomm.spring_ecomm.DTOS.product;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 50, message = "Product name must be between {min} and {max} characters")
    public String name;
    @Size(max = 200, message = "Description cannot exceed 200 characters")
    public String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    public Double price;
    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    public double discountPercentage;
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    public Integer quantity;
}
