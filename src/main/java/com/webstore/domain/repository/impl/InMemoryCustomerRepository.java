package com.webstore.domain.repository.impl;

import com.webstore.domain.Customer;
import com.webstore.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository{
    private List<Customer> listOfCustomers = new ArrayList<Customer>();

    public InMemoryCustomerRepository(){
        Customer customer1 = new Customer();
        customer1.setCustomerId("1");
        customer1.setName("Konrad");
        customer1.setAddress("Kraków");
        customer1.setNoOfOrdersMade(3);
        Customer customer2 = new Customer();
        customer2.setCustomerId("2");
        customer2.setName("Paweł");
        customer2.setAddress("Warszawa");
        customer2.setNoOfOrdersMade(5);
        listOfCustomers.add(customer1);
        listOfCustomers.add(customer2);
    }

    public List<Customer> getAllCustomers(){
        return listOfCustomers;
    }
}
