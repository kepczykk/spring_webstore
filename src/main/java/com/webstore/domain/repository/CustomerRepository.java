package com.webstore.domain.repository;

import com.webstore.domain.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> getAllCustomers();
}
