package com.kuro.coffechain.entity;

import com.kuro.coffechain.entity.base.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coffee_shops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeShop extends AbstractAuditingEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String location;
    private String contactDetails;
    private int maxQueues;
    private String openingTime;
    private String closingTime;

    @OneToMany(mappedBy = "coffeeShop", cascade = CascadeType.ALL)
    private List<MenuItem> menu;

    public UUID getId() {
        return id;
    }
}
