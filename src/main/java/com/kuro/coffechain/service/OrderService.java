package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.enums.OrderStatus;

import java.util.UUID;

public interface OrderService {
    OrderDTO processOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(UUID orderId);
    String updateOrder(UUID orderId, OrderStatus orderStatus);
}
