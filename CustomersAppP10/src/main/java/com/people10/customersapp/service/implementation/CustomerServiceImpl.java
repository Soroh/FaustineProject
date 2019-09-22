package com.people10.customersapp.service.implementation;

import com.people10.customersapp.repository.ICustomerDao;
import com.people10.customersapp.model.Customer;
import com.people10.customersapp.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private ICustomerDao customerDao;

    @Override
    public Page<Customer> getAllCustomersPaged(int pageNo) {
        return customerDao.findAll(PageRequest.of(pageNo, 10, Sort.by("lastName")));
    }

    @Override
    public List<Customer> searchCustomers(String searchTerm) {
        return customerDao.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrIpContainingOrderByLastNameAsc(searchTerm,searchTerm,searchTerm,searchTerm);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customerDao.deleteById(customerId);

    }

    @Override
    public Customer findById(Integer customerId) {
        return customerDao.findById(customerId).orElse(null);
    }

    @Override
    public Page<Customer> search(String search, int pageNo) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        return customerDao.findAll(Sort.by(Sort.Direction.ASC, "lastName"));
    }

    @Override
    public void saveAll(List<Customer> customers) {
        customerDao.saveAll(customers);
    }
}
