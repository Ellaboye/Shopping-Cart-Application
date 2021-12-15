package com.example.shoppingcartapplication.POJO;

import com.example.shoppingcartapplication.model.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemMapper {

    private Long id;
    private Long myCartItem;
    private Shop shop;
    private Long sum;
}
