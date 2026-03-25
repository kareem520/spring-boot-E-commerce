package com.ecomm.spring_ecomm.security.products;

import com.ecomm.spring_ecomm.Repositories.ProductRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.Product;
import com.ecomm.spring_ecomm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceSecurity
{
    @Autowired
    ProductRepository productRepository;

    public boolean isProductOwner(String productId){

        Product product = productRepository.findByIdAndIsActiveTrue(productId)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String sellerId = ((User)authentication.getPrincipal()).getId();

         return product.getSeller().getId().equals(sellerId);
    }
}
