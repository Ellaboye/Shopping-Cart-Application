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

    /**
     * CREATE operation on Person
     * @param customer
     * @return boolean
     * */
    public boolean createUser(Customer customer){
        boolean flag = false;

        try {
            customer.setPassword(PasswordHashing.encryptPassword(customer.getPassword()));

            Customer userData = customerRepository.findCustomerByEmail(customer.getEmail());

            if(userData == null) {
                customer.setPosition("user");
                customerRepository.save(customer);
                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * GET operation on Person
     * @param email
     * @param password
     * @return boolean(true for successful update and false on failure on post)
     * */
    public Customer loginUser(String email, String password){

        Customer userData = null;

        try {

            userData = customerRepository.findCustomerByEmail(email);

            if(userData != null){
                if(!password.equals(PasswordHashing.decryptPassword(userData.getPassword())))
                    userData = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userData;
    }
}
