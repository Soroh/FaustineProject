package com.people10.customersapp.service;

import com.people10.customersapp.model.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {
    Page<Customer> getAllCustomersPaged(int pageNo);

    List<Customer> searchCustomers(String searchTerm);

    Customer saveCustomer(Customer customer);

    void deleteCustomer(Integer customerId);

    Customer findById(Integer customerId);

    Page<Customer> search(String search, int pageNo);

    List<Customer> findAll();

    void saveAll(List<Customer> customers);
}
