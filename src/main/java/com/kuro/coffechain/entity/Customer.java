package com.kuro.coffechain.entity;

import com.kuro.coffechain.entity.base.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractAuditingEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    private String address;

    private int servedCount; // Number of times served

    private BigDecimal spentMoney;

    public UUID getId() {
        return id;
    }
}
