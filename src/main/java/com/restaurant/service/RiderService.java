package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RiderService {
    private final RiderRepository riderRepository;

    public List<Rider> getAllRiders() { return riderRepository.findAll(); }
    public Optional<Rider> findById(String id) { return riderRepository.findById(id); }
    public Rider save(Rider r) {
        if (r.getCreatedAt() == null) r.setCreatedAt(java.time.LocalDateTime.now());
        return riderRepository.save(r);
    }
    public void delete(String id) { riderRepository.deleteById(id); }
    public List<Rider> getActiveRiders() { return riderRepository.findByStatus("active"); }
}
