package com.example.shoppingcartapplication.controller;

import com.example.shoppingcartapplication.POJO.CartItemMapper;
import com.example.shoppingcartapplication.model.CartItem;
import com.example.shoppingcartapplication.model.Customer;

import com.example.shoppingcartapplication.model.Shop;
import com.example.shoppingcartapplication.service.CustomerService;
import com.example.shoppingcartapplication.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utilities.Validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CustomerController {

    private CustomerService customerService;
    private ShoppingService shoppingService;

    @Autowired
    public CustomerController(CustomerService customerService, ShoppingService shoppingService) {
        this.customerService = customerService;
        this.shoppingService = shoppingService;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showRegister(HttpServletRequest request, HttpServletResponse response,
                               Model model, HttpSession session, @ModelAttribute("user") Customer customer) {

        if (customer != null)
            model.addAttribute("person", customer);
        else
            model.addAttribute("person", new Customer());

        return "signup";
    }

    //logging customers out
    @RequestMapping(value = "/processLogout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showHome(HttpServletRequest request, HttpServletResponse response,
                           Model model, HttpSession session) {

        //request for customers  attribute to show home page
        session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) return "redirect:/";

        List<CartItemMapper> cartItem = shoppingService.getCartItemOnShopBy(customer.getId());
        //shop in store
        List<Shop> shops = shoppingService.getShop();

        Shop shop = new Shop();


        //send required data to be loaded in the page

        model.addAttribute("shop", shop);
        model.addAttribute("products", shops);
        model.addAttribute("user", customer);
        model.addAttribute("size", cartItem.size());
        model.addAttribute("customer", customer);
        model.addAttribute("cartItem", cartItem);

        return "home";
    }


    //request to show the login page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("user", new Customer());
        return "index";
    }


    //POST request to process login process
    @RequestMapping(value = "/loginProcessing", method = RequestMethod.POST)
    public String processLogin(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute("user") Customer customer, HttpSession session) {

        System.out.println(customer);
        Customer user = customerService.loginUser(customer.getEmail(), customer.getPassword());

        //redirect user to home page if details are valid and store user in session
        if (user != null) {
            session.setAttribute("user", user);
            session.removeAttribute("message");
            return "redirect:/home";
        } else {
            session.setAttribute("message", "Email or Password is wrong!!!");
            return "redirect:/";
        }
    }


    //POST request for registration
    @RequestMapping(value = "/signupProcessing", method = RequestMethod.POST)
    public String processRegistration(HttpServletRequest request, HttpServletResponse response,
                                      @ModelAttribute("person") Customer customer, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();

        //validation
        String validated = Validation.validateRegistration(customer.getEmail(), customer.getGender(), customer.getPassword(),
                customer.getPhoneNumber(), customer.getFullName());

        if (!validated.equals("Successful validation")) {
            session.setAttribute("message", validated);
            redirectAttributes.addFlashAttribute("user", customer);
            return "redirect:/signup";
        }

        if (customerService.createUser(customer)) {
            session.setAttribute("message", "Successfully Registered!!!");
        } else {
            session.setAttribute("message", "Registration failed!!! or Email already exists");
        }

        return "redirect:/signup";
    }
}