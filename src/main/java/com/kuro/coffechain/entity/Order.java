package com.kuro.coffechain.entity;

import com.kuro.coffechain.entity.base.AbstractAuditingEntity;
import com.kuro.coffechain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractAuditingEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "coffee_shop_id", nullable = false)
    private CoffeeShop coffeeShop;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "queue_position", nullable = false)
    private Integer queuePosition;

    private Date orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    public UUID getId() {
        return id;
    }
}
