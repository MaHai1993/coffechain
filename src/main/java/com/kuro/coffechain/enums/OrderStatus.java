package com.kuro.coffechain.enums;

import java.util.Arrays;

public enum OrderStatus {
    PENDING,      // Order placed, waiting for processing
    IN_PROGRESS,  // Order is being prepared
    READY,        // Order is ready for pickup
    COMPLETED,    // Order has been picked up
    CANCELED;     // Order was canceled by the customer or shop

    /**
     * Converts a string value to its corresponding OrderStatus enum.
     *
     * @param status The string representation of the order status.
     * @return The matching OrderStatus enum.
     * @throws IllegalArgumentException If the input does not match any valid status.
     */
    public static OrderStatus fromString(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid order status: " + status));
    }
}

