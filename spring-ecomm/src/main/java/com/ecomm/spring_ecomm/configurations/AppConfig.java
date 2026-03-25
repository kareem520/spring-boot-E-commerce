package com.ecomm.spring_ecomm.configurations;


import com.ecomm.spring_ecomm.DTOS.Order.OrderDTO;
import com.ecomm.spring_ecomm.DTOS.OrderItem.OrderItemDTO;
import com.ecomm.spring_ecomm.cart.CartDTO;
import com.ecomm.spring_ecomm.DTOS.cartItem.CartItemDto;
import com.ecomm.spring_ecomm.DTOS.product.ProductDTO;
import com.ecomm.spring_ecomm.helper.Calculation;
import com.ecomm.spring_ecomm.models.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class AppConfig {


//    @Bean
//    public ModelMapper modelMapper(){
//        return new ModelMapper();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);

        Converter<Product,ProductDTO>productDTOConverter = mappingContext ->  {
            Product source = mappingContext.getSource();
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(source.getId());
            productDTO.setName(source.getName());
            productDTO.setPrice(source.getPrice());
            productDTO.setDescription(source.getDescription());
            if (source.getCategory()==null){
                throw new RuntimeException("Category is null");
            }
            productDTO.setCategoryName(source.getCategory().getName());
            productDTO.setSpecialPrice(Calculation.calculateSpecialPrice(source.getPrice(), source.getDiscountPercentage()));
            productDTO.setDiscountPercentage(source.getDiscountPercentage());
            productDTO.setQuantity(source.getQuantity());
            return productDTO;
        };



        Converter<CartItem,CartItemDto>cartItemDtoConverter = mappingContext->{

            CartItem source = mappingContext.getSource();
            CartItemDto cartItemDto = new CartItemDto();

            cartItemDto.setPrice(source.getPrice());
            cartItemDto.setDiscountPercentage(source.getDiscountPercentage());
            cartItemDto.setProductId(source.getProduct().getId());
            cartItemDto.setId(source.getId());
            cartItemDto.setProductName(source.getProduct().getName());
            cartItemDto.setSpecialPrice(Calculation.calculateSpecialPrice(source.getPrice(), source.getDiscountPercentage()));
            cartItemDto.setQuantity(source.getQuantity());
            cartItemDto.setSubTotal(Calculation.calculateSubTotal(source.getSpecialPrice(),source.getQuantity()));
            return cartItemDto;
        };
        Converter<Cart, CartDTO>cartDTOConverter = mappingContext -> {
            CartDTO cartDTO = new CartDTO();
            Cart source = mappingContext.getSource();

            cartDTO.setId(source.getId());

            List<CartItemDto> cartItemDtos = source.getItems().stream()
                            .map(item->modelMapper.map(item,CartItemDto.class))
                                    .toList();

            cartDTO.setCartItems(cartItemDtos);

            Double total = 0.0;
            for (CartItemDto item:cartDTO.getCartItems()){
                total += item.getSubTotal();
            }
            cartDTO.setTotalPrice(total);

            return  cartDTO;
        };
        Converter<OrderItem, OrderItemDTO>orderItemDTOConverter = MappingContext->{

            OrderItemDTO orderItemDTO = new OrderItemDTO();
            OrderItem source = MappingContext.getSource();

            orderItemDTO.setId(source.getId());
            orderItemDTO.setQuantity(source.getQuantity());
            orderItemDTO.setProductId(source.getProduct().getId());
            orderItemDTO.setProductName(source.getProduct().getName());
            orderItemDTO.setPrice(source.getPrice());
            orderItemDTO.setDiscountPercentage(source.getDiscount());
            orderItemDTO.setOrderProductPrice(source.getOrderProductPrice());
            orderItemDTO.setSubTotal(Calculation.calculateSubTotal(source.getOrderProductPrice(),source.getQuantity()));

            return orderItemDTO;
        };
        Converter<Order, OrderDTO>orderDTOConverter = MappingContext->{

            Order source = MappingContext.getSource();
            OrderDTO orderDTO = new OrderDTO();

            orderDTO.setId(source.getId());
            orderDTO.setCreatedBy(source.getCreatedBy());
            orderDTO.setCreatedDate(source.getCreatedDate());
            orderDTO.setCustomerAddress(source.getCustomerAddress());
            orderDTO.setCustomerName(source.getCustomerName());
            orderDTO.setCustomerEmail(source.getCustomerEmail());
            orderDTO.setCustomerPhoneNumber(source.getCustomerPhoneNumber());

            List<OrderItemDTO> orderItemDTOS = source.getOrderItems()
                    .stream().map(orderItem -> modelMapper.map(orderItem,OrderItemDTO.class)).toList();
            orderDTO.setOrderItems(orderItemDTOS);

            double total = 0.0;
            for (OrderItemDTO item:orderItemDTOS){
                total += item.getSubTotal();
            }
            orderDTO.setTotalPrice(total);
            return orderDTO;
        };

        modelMapper.createTypeMap(Product.class, ProductDTO.class)
                .setConverter(productDTOConverter);

        modelMapper.createTypeMap(CartItem.class, CartItemDto.class)
                .setConverter(cartItemDtoConverter)
                .addMappings(mapper -> {
                    mapper.skip(CartItemDto::setProductName);
                });

        modelMapper.createTypeMap(Cart.class, CartDTO.class)
                .setConverter(cartDTOConverter);

        modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .setConverter(orderDTOConverter);
        modelMapper.createTypeMap(OrderItem.class, OrderItemDTO.class)
                .setConverter(orderItemDTOConverter);
    return modelMapper;
    }


}
