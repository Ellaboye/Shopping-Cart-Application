package com.example.shoppingcartapplication.repository;

import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByCustomerAndShop(Customer customer, Shop shop);

    void deleteAllByShopId(Long shopId);

    List<CartItem> findAllByCustomerId(Long personId);
}
