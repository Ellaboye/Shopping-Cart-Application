package com.example.shoppingcartapplication.service;

import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utilities.PasswordHashing;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    //create user
    public boolean createUser(Customer customer) {
        boolean flag = false;

        try {
            customer.setPassword(PasswordHashing.encryptPassword(customer.getPassword()));

            Customer userData = customerRepository.findCustomerByEmail(customer.getEmail());

            if (userData == null) {
                customer.setPosition("user");
                customerRepository.save(customer);
                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    //login users with email and password
    public Customer loginUser(String email, String password) {

        Customer userData;

        userData = customerRepository.findCustomerByEmail(email);
//        System.out.println(userData);
//        if(userData != null){
//
//            if(!password.equals(PasswordHashing.decryptPassword(userData.getPassword())))
//                userData = null;
//        }
        return userData;
    }
}
