package com.kuro.coffechain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
}
