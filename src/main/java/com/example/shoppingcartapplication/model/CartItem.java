package com.example.shoppingcartapplication.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cartItem")
public class CartItem {
    @Id
    @SequenceGenerator(
            name = "cartItem_sequence",
            sequenceName = "cartItem_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "cartItem_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(
            name = "myCartItem",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long myCartItem;

    @OneToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private CartItem cartItem;

    @ManyToOne
    @JoinColumn(name = "shopCartId", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "customerCartId", referencedColumnName = "id")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMyCartItem() {
        return myCartItem;
    }

    public void setMyCartItem(Long myCartItem) {
        this.myCartItem = myCartItem;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
