package com.kuro.coffechain.ut.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.dto.CustomerDTO;
import com.kuro.coffechain.dto.ItemDTO;
import com.kuro.coffechain.entity.*;
import com.kuro.coffechain.enums.OrderStatus;
import com.kuro.coffechain.exception.NotFoundException;
import com.kuro.coffechain.repository.*;
import com.kuro.coffechain.service.OrderServiceImpl;
import com.kuro.coffechain.utils.DTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DTOConverter dtoConverter;
    @Mock private CustomerRepository customerRepository;
    @Mock private MenuItemRepository menuItemRepository;
    @Mock private CoffeeShopRepository coffeeShopRepository;

    @InjectMocks private OrderServiceImpl orderService;

    private OrderDTO orderDTO;
    private Order order;
    private Customer customer;
    private CoffeeShop coffeeShop;
    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        // Prepare test data
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID shopId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setMobileNumber("123456789");
        customer.setAddress("123 Coffee Street");
        customer.setServedCount(2);
        customer.setSpentMoney(BigDecimal.ZERO);

        coffeeShop = new CoffeeShop();
        coffeeShop.setId(shopId);
        coffeeShop.setName("Test Coffee Shop");

        MenuItem menuItem = new MenuItem();
        menuItem.setId(itemId);
        menuItem.setPrice(5.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(5.0));

        orderItems = List.of(orderItem);

        order = new Order();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setCoffeeShop(coffeeShop);
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);
        order.setQueuePosition(1);
        order.setOrderTime(new Date());

        orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setCoffeeShopId(shopId);
        orderDTO.setOrderStatus(OrderStatus.PENDING.name());

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerId);
        customerDTO.setCustomerName("John Doe");
        customerDTO.setCustomerPhone("123456789");
        customerDTO.setCustomerAddress("123 Coffee Street");

        orderDTO.setCustomerDTO(customerDTO);
        orderDTO.setItemDTO(List.of(new ItemDTO(itemId, 2)));
    }

    @Test
    void testProcessOrder_Success() {
        // Mock repository responses
        when(customerRepository.findByMobileNumber(any())).thenReturn(Optional.of(customer));
        when(coffeeShopRepository.findById(orderDTO.getCoffeeShopId())).thenReturn(Optional.of(coffeeShop));
        when(menuItemRepository.findById(any(UUID.class))).thenReturn(Optional.of(new MenuItem(UUID.fromString("d3f6c987-9a6b-4a36-b1f4-1c78eecfdf98"), "coffee", 10.000, coffeeShop)));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

        // Invoke method
        OrderDTO result = orderService.processOrder(orderDTO);

        // Verify results
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING.name(), result.getOrderStatus());
        assertEquals(orderDTO.getCoffeeShopId(), result.getCoffeeShopId());

        // Verify repository calls
        verify(customerRepository).saveAndFlush(any(Customer.class));
        verify(orderRepository).saveAndFlush(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() {
        // Mock repository response
        when(orderRepository.findById(orderDTO.getId())).thenReturn(Optional.of(order));
        when(dtoConverter.convertToDTO(order, OrderDTO.class)).thenReturn(orderDTO);

        // Invoke method
        OrderDTO result = orderService.getOrderById(orderDTO.getId());

        // Verify results
        assertNotNull(result);
        assertEquals(orderDTO.getId(), result.getId());

        // Verify repository call
        verify(orderRepository).findById(orderDTO.getId());
    }

    @Test
    void testGetOrderById_NotFound() {
        // Mock repository response
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Invoke method and verify exception
        Exception exception = assertThrows(NotFoundException.class, () -> orderService.getOrderById(UUID.randomUUID()));
        assertEquals("Order not found. Please check again!", exception.getMessage());

        verify(orderRepository).findById(any(UUID.class));
    }

    @Test
    void testUpdateOrder_Success() {
        // Mock repository response
        when(orderRepository.findById(orderDTO.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Invoke method
        String result = orderService.updateOrder(orderDTO.getId(), OrderStatus.READY);

        // Verify results
        assertEquals("Order with id " + orderDTO.getId() + " has been updated", result);
        assertEquals(OrderStatus.READY, order.getStatus());

        // Verify repository calls
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdateOrder_NotFound() {
        // Mock repository response
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Invoke method and verify exception
        Exception exception = assertThrows(NotFoundException.class, () -> orderService.updateOrder(UUID.randomUUID(), OrderStatus.READY));
        assertEquals("Order not found. Please check again!", exception.getMessage());

        verify(orderRepository).findById(any(UUID.class));
    }

    @Test
    void testGetPendingOrderAndTime() {
        // Mock repository response
        when(orderRepository.countPendingOrdersByCoffeeShop(any(UUID.class))).thenReturn(5);

        // Call private method using Reflection
        @SuppressWarnings("unchecked")
        var pendingOrder = (Pair<Integer, String>) ReflectionTestUtils.invokeMethod(orderService, "getPendingOrderAndTime", UUID.randomUUID());

        // Verify results
        assertNotNull(pendingOrder);
        assertEquals(6, pendingOrder.getFirst());
        assertTrue(pendingOrder.getSecond().contains("50 minutes"));
    }
}

