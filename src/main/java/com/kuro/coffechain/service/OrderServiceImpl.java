package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.entity.*;
import com.kuro.coffechain.enums.OrderStatus;
import com.kuro.coffechain.exception.NotFoundException;
import com.kuro.coffechain.repository.CoffeeShopRepository;
import com.kuro.coffechain.repository.CustomerRepository;
import com.kuro.coffechain.repository.MenuItemRepository;
import com.kuro.coffechain.repository.OrderRepository;
import com.kuro.coffechain.utils.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DTOConverter dtoConverter;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    @Transactional
    public OrderDTO processOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerDTO().getCustomerId())
                .orElse(null);

        if (customer == null) {
            customer = Customer.builder()
                    .name(orderDTO.getCustomerDTO().getCustomerName())
                    .mobileNumber(orderDTO.getCustomerDTO().getCustomerPhone())
                    .address(orderDTO.getCustomerDTO().getCustomerAddress())
                    .servedCount(0)
                    .build();
        }
        // increase count time for customer to have discount later.
        customer.setServedCount(customer.getServedCount() + 1);
        Customer finalCustomer = customer;

        CoffeeShop coffeeShop = coffeeShopRepository.findById(orderDTO.getCoffeeShopId())
                .orElseThrow(() -> new NotFoundException("Shop not found. Please check again!"));

        var pendingOrder = getPendingOrderAndTime(orderDTO.getCoffeeShopId());
        Order order = new Order();
        order.setCoffeeShop(coffeeShop);
        List<OrderItem> orderItems = new ArrayList<>();
        Order finalOrder = order;

        orderDTO.getItemDTO().forEach(orderDTOItemDTO -> {
            MenuItem menuItem = menuItemRepository.findById(orderDTOItemDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Item not found in the menu. Please check again!"));
            // Get current spent money, add new amount
            BigDecimal currentSpentMoney = finalCustomer.getSpentMoney() != null ? finalCustomer.getSpentMoney() : BigDecimal.ZERO;
            BigDecimal newAmount = BigDecimal.valueOf(menuItem.getPrice() * orderDTOItemDTO.getQuantity());
            finalCustomer.setSpentMoney(currentSpentMoney.add(newAmount));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(orderDTOItemDTO.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(menuItem.getPrice()));
            orderItem.setOrder(finalOrder);
            orderItems.add(orderItem);
        });
        customerRepository.save(finalCustomer);
        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setQueuePosition(pendingOrder.getFirst());
        order.setOrderItems(orderItems);

        order = orderRepository.saveAndFlush(order);

        OrderDTO processingOrderSuccess = new OrderDTO();
        processingOrderSuccess.setId(order.getId());
        processingOrderSuccess.setOrderStatus(OrderStatus.PENDING.name());
        processingOrderSuccess.setWaitingTime(pendingOrder.getSecond());
        return new OrderDTO();
    }

    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return dtoConverter.convertToDTO(order, OrderDTO.class);
    }

    /**
     * Retrieves the number of pending orders for a given coffee shop and calculates
     * the estimated waiting time for a new order.
     *
     * @param coffeeShopId The unique identifier of the coffee shop.
     * @return A {@link Pair} where:
     * - The first value is the number of pending orders plus one (to account for the new order).
     * - The second value is a message indicating the estimated waiting time in minutes.
     */
    private Pair<Integer, String> getPendingOrderAndTime(UUID coffeeShopId) {
        int pendingOrders = orderRepository.countPendingOrdersByCoffeeShop(coffeeShopId);
        int estimatedTime = (pendingOrders * 10); // Each order takes 10 minutes
        return Pair.of(pendingOrders + 1, "Please take your order after:" + estimatedTime + " minutes");
    }
}
