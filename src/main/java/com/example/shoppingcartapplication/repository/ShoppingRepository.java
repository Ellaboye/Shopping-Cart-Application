package com.example.shoppingcartapplication.repository;

import com.example.shoppingcartapplication.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ShoppingRepository extends JpaRepository<Shop, Long> {
}
