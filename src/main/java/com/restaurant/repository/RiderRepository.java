package com.restaurant.repository;

import com.restaurant.model.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RiderRepository extends MongoRepository<Rider, String> {
    List<Rider> findByStatus(String status);
    List<Rider> findByVehicleType(String vehicleType);
}
