package com.restaurant.repository;

import com.restaurant.model.SupplierPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SupplierPaymentRepository extends MongoRepository<SupplierPayment, String> {
    List<SupplierPayment> findBySupplierId(String supplierId);
    List<SupplierPayment> findByStatus(String status);
}
