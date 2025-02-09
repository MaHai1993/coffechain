package com.kuro.coffechain.controller;

import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.enums.OrderStatus;
import com.kuro.coffechain.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/items")
    public ResponseEntity<OrderDTO> order(@RequestBody OrderDTO orderDTO) {
        OrderDTO processingOrder = orderService.processOrder(orderDTO);
        return ResponseEntity.ok(processingOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody OrderDTO orderDTO) {

        String result = orderService.updateOrder(orderId, OrderStatus.fromString(orderDTO.getOrderStatus()));
        return ResponseEntity.ok(result);
    }

}
