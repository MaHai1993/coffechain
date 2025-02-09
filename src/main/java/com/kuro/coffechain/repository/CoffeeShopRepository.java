package com.kuro.coffechain.repository;

import com.kuro.coffechain.entity.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, UUID> {
}
