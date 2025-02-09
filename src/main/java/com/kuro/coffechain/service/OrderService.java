package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.OrderDTO;

import java.util.UUID;

public interface OrderService {
    OrderDTO processOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(UUID orderId);
}
