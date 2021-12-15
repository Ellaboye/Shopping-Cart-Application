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

    /**
     * Delete request on orders made by users
     * redirects user if not in session
     * delete orders in the database, or perhaps fails
     * redirect back to home page
     * */
    @RequestMapping(value = "/deleteOrder/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) return "redirect:/";

        if( cartItemService.deleteOrder(id)){
            session.setAttribute("message", "Successfully deleted Order!!!");
        }else{
            session.setAttribute("message", "failed to delete Order!!!");
        }

        return "redirect:/home";
    }
}
