package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierPaymentRepository paymentRepository;

    public List<Supplier> getAllSuppliers() { return supplierRepository.findAll(); }
    public Optional<Supplier> findById(String id) { return supplierRepository.findById(id); }
    public Supplier save(Supplier s) { return supplierRepository.save(s); }
    public void delete(String id) { supplierRepository.deleteById(id); }
    public List<Supplier> getActiveSuppliers() { return supplierRepository.findByStatus("active"); }
    public List<SupplierPayment> getPayments(String supplierId) { return paymentRepository.findBySupplierId(supplierId); }
    public SupplierPayment savePayment(SupplierPayment p) { return paymentRepository.save(p); }
    public List<SupplierPayment> getAllPayments() { return paymentRepository.findAll(); }
}
