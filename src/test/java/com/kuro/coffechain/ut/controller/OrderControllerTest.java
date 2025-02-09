package com.kuro.coffechain.ut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuro.coffechain.controller.OrderController;
import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.dto.CustomerDTO;
import com.kuro.coffechain.enums.OrderStatus;
import com.kuro.coffechain.service.OrderService;
import com.kuro.coffechain.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID coffeeShopId = UUID.randomUUID();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerId);
        customerDTO.setCustomerName("Kuro Ma");
        customerDTO.setCustomerPhone("+843456789");
        customerDTO.setCustomerAddress("123 Street");

        orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setCustomerDTO(customerDTO);
        orderDTO.setCoffeeShopId(coffeeShopId);
        orderDTO.setOrderStatus(OrderStatus.PENDING.name());
    }

    @Test
    void testOrder_Success() throws Exception {
        when(orderService.processOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/order/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())  // Check status 200
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));
    }

    @Test
    void testUpdateOrder_Success() throws Exception {
        when(orderService.updateOrder(any(UUID.class), any(OrderStatus.class)))
                .thenReturn("Order updated successfully");

        mockMvc.perform(put("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated successfully"));  // Check response message
    }
}

