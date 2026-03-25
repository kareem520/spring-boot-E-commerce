package com.ecomm.spring_ecomm.services;

import com.ecomm.spring_ecomm.AuthUtil.AuthUtil;
import com.ecomm.spring_ecomm.DTOS.Order.OrderDTO;
import com.ecomm.spring_ecomm.DTOS.cartItem.CartItemDto;
import com.ecomm.spring_ecomm.Repositories.CartItemRepository;
import com.ecomm.spring_ecomm.Repositories.OrderItemRepository;
import com.ecomm.spring_ecomm.Repositories.OrderRepository;
import com.ecomm.spring_ecomm.cart.CartService;
import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import com.ecomm.spring_ecomm.models.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    AuthUtil  authUtil;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    User ourUser;

    @Override
    public List<OrderDTO> getMyOrders() {
        ourUser = authUtil.loggedInUser();
        if (ourUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        List<Order> orders = orderRepository.findByUser_idAllWithOrderItems(ourUser.getId());
        if  (orders.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ORDERS_FOUND);
        }
        return orders.stream()
                .map(order->modelMapper.map(order,OrderDTO.class)).toList();
    }


    @Override
    public List<OrderDTO> getAllOrdersForAllCustomers() {
        List<Order> orders = orderRepository.findAllWithOrderItems();

        return orders.stream()
                .map(order -> modelMapper.map(order,OrderDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public OrderDTO placeMyOrder(String customerEmail, String customerAddress,String customerName,String customerPhoneNumber) {
        ourUser = authUtil.loggedInUser();
        if (ourUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Cart ourCart = ourUser.getCart();
        if (ourCart == null) {
            throw new BusinessException(ErrorCode.USER_Cart_FOUND);
        }

        if (ourCart.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ITEMS_TO_PLACE);
        }


        Order order = new Order();

        order.setUser(ourUser);
        if (customerEmail==null || customerEmail.isEmpty())order.setCustomerEmail(ourUser.getEmail());
        else order.setCustomerEmail(customerEmail);

        if (customerAddress==null || customerAddress.isEmpty())order.setCustomerAddress("Cairo");
        else order.setCustomerAddress(customerAddress);

        if(customerPhoneNumber==null || customerPhoneNumber.isEmpty())order.setCustomerPhoneNumber(ourUser.getPhoneNumber());
        else order.setCustomerPhoneNumber(customerPhoneNumber);

        if (customerName==null || customerName.isEmpty())order.setCustomerName(ourUser.getFirstName() + " " + ourUser.getLastName());
        else order.setCustomerEmail(customerEmail);

        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        List<CartItem> cartItems = new ArrayList<>(ourCart.getItems()); // clone list

        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                throw new BusinessException(ErrorCode.NO_ENOUGH_QUANTITY_FOUND, cartItem.getProductName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderProductPrice(cartItem.getSpecialPrice());
            orderItem.setSubTotal(cartItem.getQuantity() * cartItem.getSpecialPrice());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            // remove from cart to trigger orphanRemoval
            cartItem.getCart().getItems().remove(cartItem);
            cartItem.setCart(null);
            cartItem.setProduct(null);

            orderItemRepository.save(orderItem);

            // update product quantity
            productService.takeQuantityFromProduct(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        order.setOrderItems(orderItems);

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        order.setTotalPrice(orderDTO.getTotalPrice());

        return orderDTO;

    }
}
