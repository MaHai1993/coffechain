package com.kuro.coffechain.controller;

import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.service.OrderService;
import com.kuro.coffechain.utils.JwtUtil;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/items")
    public ResponseEntity<OrderDTO> order(@RequestBody OrderDTO orderDTO) {
        OrderDTO processingOrder = orderService.processOrder(orderDTO);
        return ResponseEntity.ok(processingOrder);
    }
}
