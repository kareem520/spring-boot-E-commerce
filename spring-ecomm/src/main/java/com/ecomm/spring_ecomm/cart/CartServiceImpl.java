package com.ecomm.spring_ecomm.cart;

import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.cartItem.CartItemDto;
import com.ecomm.spring_ecomm.Repositories.CartItemRepository;
import com.ecomm.spring_ecomm.Repositories.CartRepository;
import com.ecomm.spring_ecomm.Repositories.ProductRepository;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.helper.Calculation;
import com.ecomm.spring_ecomm.models.Cart;
import com.ecomm.spring_ecomm.models.CartItem;
import com.ecomm.spring_ecomm.models.Product;
import com.ecomm.spring_ecomm.models.User;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthUtil authUtil;
    @Autowired
    CartItemRepository cartItemRepository;

    User ourUser = null;

    @Override
    public CartDTO getCartByEmail(String email) {
        Cart cart = cartRepository.findByUser_Email(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_Cart_FOUND));

        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartDTO addProductToCart(String productId, int quantity) {

        this.ourUser = authUtil.loggedInUser();

        if (ourUser.getCart()==null){
            createCart();
        }
        Cart ourCart =  ourUser.getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));
        CartItem cartItem = cartItemRepository.findByProduct_idAndCart_id(productId,ourCart.getId())
                .orElse(new CartItem());

        if (cartItem.getQuantity()!=0){
            throw new BusinessException(ErrorCode.PRODUCT_ALREADY_EXISTS_IN_CART,productId);
        }

        if (product.getQuantity()==0){
            throw new BusinessException(ErrorCode.PRODUCT_NOT_AVAILABLE_IN_STOCK);
        }
        if (product.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.NO_ENOUGH_QUANTITY_FOUND,product.getName());
        }


        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCart(ourCart);
        ourCart.getItems().add(cartItem);//important
        cartItem.setPrice(product.getPrice());
        cartItem.setProductName(product.getName());
        cartItem.setDiscountPercentage(product.getDiscountPercentage());
        cartItem.setSpecialPrice(Calculation.calculateSpecialPrice(product.getPrice(), product.getDiscountPercentage()));
        cartItemRepository.save(cartItem);

        CartDTO cartDTO = modelMapper.map(ourCart, CartDTO.class);
        //must be converted to DTO first before set this total price ,because in-
        // it we calculate the total price (look at custom mapping in appConfig)
        ourCart.setTotalPrice(cartDTO.getTotalPrice());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getCarts() {

        List<Cart>carts = cartRepository.findAllWithItems();

        List<CartDTO> cartDTOs = carts.stream()
                .map(cart -> modelMapper.map(cart,CartDTO.class)).toList();
        return cartDTOs;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(String productId, Integer quantity) {
        this.ourUser = authUtil.loggedInUser();

        Cart ourCart = ourUser.getCart();

        if (ourCart == null){
            throw  new BusinessException(ErrorCode.USER_Cart_FOUND,"Cart");
        }

        CartItem cartItem = cartItemRepository.findByProduct_idAndCart_id(productId,ourCart.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.NO_CART_ITEM_FOUND, productId));

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        if (quantity==0){
            deleteProductFromCart(productId);
        }
        else {
            if (cartItem.getQuantity() < quantity) {
                Integer remainingQuantity = quantity - cartItem.getQuantity();
                if (product.getQuantity() < remainingQuantity) {
                    throw new BusinessException(ErrorCode.NO_ENOUGH_QUANTITY_FOUND, product.getName());
                }
            }
            cartItem.setQuantity(quantity);

        }
        CartDTO cartDTO = modelMapper.map(ourCart, CartDTO.class);
        //must be converted to DTO first before set this total price ,because in-
        // it we calculate the total price (look at custom mapping in appConfig)
        ourCart.setTotalPrice(cartDTO.getTotalPrice());

        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(String productId) {
        this.ourUser = authUtil.loggedInUser();

        Cart ourCart = ourUser.getCart();

        if (ourCart == null){
            throw  new BusinessException(ErrorCode.USER_Cart_FOUND,"Cart");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new BusinessException(ErrorCode.ENTITY_NOT_FOUND,"Product",productId));

        CartItem cartItem = cartItemRepository.findByProduct_idAndCart_id(productId,ourCart.getId())
                .orElseThrow(()->new BusinessException(ErrorCode.NO_CART_ITEM_FOUND, productId));
        CartItemDto  cartItemDto = modelMapper.map(cartItem, CartItemDto.class);

        ourCart.setTotalPrice(ourCart.getTotalPrice() - cartItemDto.getSubTotal());

        cartItemRepository.deleteByProduct_idAndCart_id(productId,ourCart.getId());

        return "Product removed successfully From Cart: " + ourCart.getId();

    }

    @Transactional
    private Cart createCart() {
        Cart cart = new Cart();
        //get out user
        cart.setUser(ourUser);
        cartRepository.save(cart);
        return cart;
    }
}
