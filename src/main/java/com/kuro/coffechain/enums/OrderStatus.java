package com.kuro.coffechain.enums;

public enum OrderStatus {
    PENDING,      // Order placed, waiting for processing
    IN_PROGRESS,  // Order is being prepared
    READY,        // Order is ready for pickup
    COMPLETED,    // Order has been picked up
    CANCELED      // Order was canceled by the customer or shop
}
