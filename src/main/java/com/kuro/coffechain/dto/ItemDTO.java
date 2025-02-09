package com.kuro.coffechain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemDTO {
    private UUID id;
    private int quantity;
}
