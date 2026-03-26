package com.ecomm.spring_ecomm.security.cart;

import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.Repositories.CartRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.Product;
import com.ecomm.spring_ecomm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartServiceSecurity {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil  authUtil;


}
