package com.example.shoppingcartapplication.service;

import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.model.Shop;
import com.example.shoppingcartapplication.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;


    //order by person on a particular product
    public CartItem getCartItem(Customer customer, Shop shop) {
        CartItem cartItem = null;

        try {
            cartItem = cartItemRepository.findCartItemByCustomerAndShop(customer, shop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartItem;
    }


    //add orders made by users on a product
    public boolean addCartItem(Customer customer, Shop shop) {
        boolean flag = false;

        try {
            CartItem cartItem = cartItemRepository.findCartItemByCustomerAndShop(customer, shop);

            if (cartItem == null) {
                CartItem newCartItem = new CartItem();
                Long count = 1L;
                newCartItem.setMyCartItem(count);
                newCartItem.setCustomer(customer);
                newCartItem.setShop(shop);

                cartItemRepository.save(newCartItem);

            } else {
                cartItem.setMyCartItem(cartItem.getMyCartItem() + 1);
                cartItem.setCustomer(customer);
                cartItem.setShop(shop);

                cartItemRepository.save(cartItem);
            }

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    //delete order
    public boolean deleteOrder(Long orderId) {
        boolean flag = false;

        try {
            cartItemRepository.deleteById(orderId);

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

}
