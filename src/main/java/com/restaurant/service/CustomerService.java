package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }
    public Optional<Customer> findById(String id) { return customerRepository.findById(id); }
    public Customer save(Customer c) { return customerRepository.save(c); }
    public void delete(String id) { customerRepository.deleteById(id); }
    public List<Customer> search(String q) { return customerRepository.findByNameContainingIgnoreCase(q); }
    public long countByStatus(String s) { return customerRepository.countByStatus(s); }

    public double getTotalRevenue() {
        return customerRepository.findAll().stream()
            .mapToDouble(c -> c.getTotalSpent() != null ? c.getTotalSpent() : 0).sum();
    }

    public double getAvgOrderValue() {
        List<Customer> all = customerRepository.findAll();
        long totalOrders = all.stream().mapToLong(c -> c.getTotalOrders() != null ? c.getTotalOrders() : 0).sum();
        double totalSpent = all.stream().mapToDouble(c -> c.getTotalSpent() != null ? c.getTotalSpent() : 0).sum();
        return totalOrders > 0 ? totalSpent / totalOrders : 0;
    }
}
