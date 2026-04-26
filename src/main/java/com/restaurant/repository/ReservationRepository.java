package com.restaurant.repository;

import com.restaurant.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByBranch(String branch);
    List<Reservation> findByDate(LocalDate date);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByTableId(String tableId);
    List<Reservation> findByDateBetween(LocalDate start, LocalDate end);
}
