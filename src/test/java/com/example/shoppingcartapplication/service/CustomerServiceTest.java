package com.example.shoppingcartapplication.service;

import com.example.shoppingcartapplication.model.Customer;
import com.example.shoppingcartapplication.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(10L);
        customer.setFullName("Stella");
        customer.setEmail("info@decagonhq.com");
        customer.setPassword("123456");
        customer.setGender("Female");
    }

    @Test
    void shouldTestForCreationOfUser() {
        //mock customerRepo
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //call method to be tested
        customerService.createUser(customer);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldTestForlogginUser() {
        //mock customer repo
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(customer);
        //call method to be tested
        Customer customer = customerService.loginUser("desadfs", "uwrbhrtn");


        verify(customerRepository, times(1)).findCustomerByEmail(anyString());
    }
}
