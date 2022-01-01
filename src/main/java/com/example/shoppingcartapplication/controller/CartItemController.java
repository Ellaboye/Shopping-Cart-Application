package com.example.shoppingcartapplication.controller;

import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.service.CartItemService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class CartItemController {


    private CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @RequestMapping(value = "/deleteOrder/{id}", method = RequestMethod.GET)
    //delete request on orders made by customers
    public String delete(@PathVariable("id") Long id, HttpSession session) {

        //redirect the customer if is not in session
        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) return "redirect:/";

        //delete orders when its been picked and should be deleted in the database
        if (cartItemService.deleteOrder(id)) {
            session.setAttribute("message", "Successfully deleted Order!!!");
        } else {
            session.setAttribute("message", "failed to delete Order!!!");
        }

        return "redirect:/home";
    }
}
