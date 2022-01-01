package com.example.shoppingcartapplication.controller;

import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.model.Shop;
import com.example.shoppingcartapplication.service.CartItemService;
import com.example.shoppingcartapplication.service.ShoppingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ShoppingController {


    private ShoppingService shoppingService;
    private CartItemService cartItemService;

    public ShoppingController(ShoppingService shoppingService, CartItemService cartItemService) {
        this.shoppingService = shoppingService;
        this.cartItemService = cartItemService;
    }


    //POST request to post products
    @RequestMapping(value = "/adminFormProcessing", method = RequestMethod.POST)
    public String processProductUpload(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("product") Shop shop, @RequestParam("file") MultipartFile imageFile, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("user");

        //redirect customer if not in session
        if (customer == null) return "redirect:/";

        //set person who made the product which is the admin
        shop.setCustomer(customer);

        if (shoppingService.saveShop(imageFile, shop)) {
            session.setAttribute("message", "Successfully Posted!!!");
        } else {
            session.setAttribute("message", "Failed to Post!!!");
        }

        return "redirect:/home";
    }


    //GET request to display a particular product
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String showProductInfo(@PathVariable("id") Long id, Model model, HttpSession session) {

        Shop shop = shoppingService.getShopById(id);

        Customer customer = (Customer) session.getAttribute("user");

        CartItem cartItem = cartItemService.getCartItem(customer, shop);
        Long numberOfOrders = 0L;

        if (cartItem != null) {
            numberOfOrders = cartItem.getMyCartItem();
        }

        //redirect users if not in session
        if (customer == null) return "redirect:/";

        //orders made on the product is displayed
        model.addAttribute("product", shop);
        model.addAttribute("orders", numberOfOrders);

        return "product-details";
    }


    //POST request to add product to cart
    @RequestMapping(value = "/add_to_cart/{id}", method = RequestMethod.POST)
    public @ResponseBody
    String addCart(@PathVariable("id") Long id, Model model, HttpSession session) {

        Shop shop = shoppingService.getShopById(id);

        //redirect user if not in session
        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) return "redirect:/";

        //save orders in the database or perhaps fails
        boolean success = cartItemService.addCartItem(customer, shop);
        if (success) {
            System.out.println("success");
            return "success";
        } else {
            System.out.println("Not successful");
            return "failed";
        }
    }


    //edit request
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("user");

        //redirect customer if not in session
        if (customer == null) return "redirect:/";

        Shop shop = shoppingService.getShopById(id);

        model.addAttribute("products", shop);
        return "edit";
    }

    /*Edit post processing */
    @RequestMapping(value = "/editPostProcessing", method = RequestMethod.POST)
    public String edit(@ModelAttribute("product") Shop shop, HttpSession session, @RequestParam("file") MultipartFile imageFile) {

        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) return "redirect:/";

        //edit product in the database
        if (shoppingService.editShop(imageFile, shop)) {
            session.setAttribute("message", "Successfully edited Posted!!!");
        } else {
            session.setAttribute("message", "failed to edit Posted!!!");
        }

        return "redirect:/home";
    }


    //Delete product
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("user");
        if (customer == null) return "redirect:/";

        if (shoppingService.deleteShop(id)) {
            session.setAttribute("message", "Successfully deleted Product!!!");
        } else {
            session.setAttribute("message", "failed to delete Product!!!");
        }

        return "redirect:/home";
    }

}