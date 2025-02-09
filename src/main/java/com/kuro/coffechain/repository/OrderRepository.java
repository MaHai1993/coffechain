package com.kuro.coffechain.repository;

import com.kuro.coffechain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT COUNT(o) FROM Order o WHERE o.coffeeShop.id = :coffeeShopId AND o.status = 'PENDING'")
    int countPendingOrdersByCoffeeShop(@Param("coffeeShopId") UUID coffeeShopId);
}
