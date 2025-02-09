package com.kuro.coffechain.repository;

import com.kuro.coffechain.entity.Order;
import com.kuro.coffechain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT COUNT(o) FROM Order o WHERE o.coffeeShop.id = :coffeeShopId AND o.status = 'PENDING'")
    int countPendingOrdersByCoffeeShop(@Param("coffeeShopId") UUID coffeeShopId);

    @Transactional
    @Modifying
    @Query("update Order o set o.status = ?1 where o.id = ?2")
    int updateStatusById(OrderStatus status, UUID id);
}
