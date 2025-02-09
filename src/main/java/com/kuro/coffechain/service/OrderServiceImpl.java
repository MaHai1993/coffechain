package com.kuro.coffechain.service;

import com.kuro.coffechain.dto.CustomerDTO;
import com.kuro.coffechain.dto.OrderDTO;
import com.kuro.coffechain.entity.*;
import com.kuro.coffechain.enums.OrderStatus;
import com.kuro.coffechain.exception.NotFoundException;
import com.kuro.coffechain.repository.CoffeeShopRepository;
import com.kuro.coffechain.repository.CustomerRepository;
import com.kuro.coffechain.repository.MenuItemRepository;
import com.kuro.coffechain.repository.OrderRepository;
import com.kuro.coffechain.utils.DTOConverter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.kuro.coffechain.constants.Contants.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DTOConverter dtoConverter;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            DTOConverter dtoConverter,
                            CustomerRepository customerRepository,
                            MenuItemRepository menuItemRepository,
                            CoffeeShopRepository coffeeShopRepository) {
        this.orderRepository = orderRepository;
        this.dtoConverter = dtoConverter;
        this.customerRepository = customerRepository;
        this.menuItemRepository = menuItemRepository;
        this.coffeeShopRepository = coffeeShopRepository;
    }

    @Transactional
    public OrderDTO processOrder(OrderDTO orderDTO) {
        Customer finalCustomer = upsertCustomer(orderDTO);
        CoffeeShop coffeeShop = coffeeShopRepository.findById(orderDTO.getCoffeeShopId())
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));

        var pendingOrder = getPendingOrderAndTime(orderDTO.getCoffeeShopId());
        Order order = new Order();
        order.setCoffeeShop(coffeeShop);
        List<OrderItem> orderItems = new ArrayList<>();
        Order finalOrder = order;

        orderDTO.getItemDTO().forEach(orderDTOItemDTO -> {
            MenuItem menuItem = menuItemRepository.findById(orderDTOItemDTO.getId())
                    .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));
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

        customerRepository.saveAndFlush(finalCustomer);
        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setQueuePosition(pendingOrder.getFirst());
        order.setOrderItems(orderItems);
        order.setCustomer(finalCustomer);
        order = orderRepository.saveAndFlush(order);

        return buildProcessingOrderDTO(orderDTO, order, pendingOrder);
    }

    @Override
    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
        return dtoConverter.convertToDTO(order, OrderDTO.class);
    }

    @Override
    public String updateOrder(UUID orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND));
        order.setStatus(orderStatus);
        orderRepository.save(order);
        return "Order with id " + orderId + " has been updated";
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
        return Pair.of(pendingOrders + 1, "Please take your order after: " + estimatedTime + " minutes");
    }


    /**
     * Retrieves an existing customer or creates a new one if not found.
     * <p>
     * If the customer exists, it increments the served count.
     * If the customer does not exist, a new customer is created with the provided details.
     * </p>
     *
     * @param orderDTO The order data containing customer details.
     * @return The retrieved or newly created {@link Customer}.
     */
    private Customer upsertCustomer(OrderDTO orderDTO) {
        CustomerDTO customerDTO = orderDTO.getCustomerDTO();

        // Attempt to find an existing customer phone number
        Customer customer = customerRepository.findByMobileNumber(customerDTO.getCustomerPhone()).orElseGet(() -> createNewCustomer(customerDTO));

        // Increment served count to track customer visits
        customer.setServedCount(customer.getServedCount() + 1);

        return customer;
    }

    /**
     * Creates a new customer instance with the provided details.
     *
     * @param customerDTO The customer data transfer object containing the necessary information.
     * @return A new {@link Customer} instance.
     */
    private Customer createNewCustomer(CustomerDTO customerDTO) {
        return Customer.builder()
                .name(customerDTO.getCustomerName())
                .mobileNumber(customerDTO.getCustomerPhone())
                .address(customerDTO.getCustomerAddress())
                .servedCount(0)
                .build();
    }

    private static OrderDTO buildProcessingOrderDTO(OrderDTO orderDTO, Order order, Pair<Integer, String> pendingOrder) {
        OrderDTO processingOrderSuccess = new OrderDTO();
        processingOrderSuccess.setId(order.getId());
        processingOrderSuccess.setOrderStatus(OrderStatus.PENDING.name());
        processingOrderSuccess.setWaitingTime(pendingOrder.getSecond());
        processingOrderSuccess.setCoffeeShopId(orderDTO.getCoffeeShopId());
        return processingOrderSuccess;
    }
}
