package com.kuro.coffechain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private UUID id;
    private CustomerDTO customerDTO;
    private UUID coffeeShopId;
    private List<ItemDTO> itemDTO;
    private String orderStatus;
    private String waitingTime;
    private BigDecimal totalPrice;
}
