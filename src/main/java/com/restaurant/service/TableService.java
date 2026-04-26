package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import com.restaurant.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public List<RestaurantTable> getAllTables() { return tableRepository.findAll(); }
    public Optional<RestaurantTable> findById(String id) { return tableRepository.findById(id); }

    public RestaurantTable save(RestaurantTable table) {
        if (table.getQrCode() == null || table.getQrCode().isEmpty()) {
            String url = baseUrl + "/menu/public?table=" + table.getTableNumber();
            table.setQrUrl(url);
            table.setQrCode(QRCodeUtil.generateQRCodeBase64(url, 200, 200));
        }
        return tableRepository.save(table);
    }

    public void delete(String id) { tableRepository.deleteById(id); }

    public RestaurantTable changeStatus(String id, String newStatus) {
        RestaurantTable table = tableRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(newStatus);
        return tableRepository.save(table);
    }

    public List<Reservation> getAllReservations() { return reservationRepository.findAll(); }
    public Reservation saveReservation(Reservation r) { return reservationRepository.save(r); }
    public void deleteReservation(String id) { reservationRepository.deleteById(id); }

    public long countByStatus(String status) { return tableRepository.countByStatus(status); }
}
