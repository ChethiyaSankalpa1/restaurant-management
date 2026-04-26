package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final ShiftRepository shiftRepository;

    public List<Staff> getAllStaff() { return staffRepository.findAll(); }
    public Optional<Staff> findById(String id) { return staffRepository.findById(id); }
    public Staff save(Staff s) { return staffRepository.save(s); }
    public void delete(String id) { staffRepository.deleteById(id); }
    public long countByStatus(String status) { return staffRepository.countByStatus(status); }
    public List<Shift> getRecentShifts() {
        return shiftRepository.findAll().stream()
            .sorted((a, b) -> b.getDate() != null && a.getDate() != null ? b.getDate().compareTo(a.getDate()) : 0)
            .limit(20).toList();
    }
    public Shift saveShift(Shift shift) { return shiftRepository.save(shift); }
    public double getTotalSalary() {
        return staffRepository.findByStatus("active").stream()
            .mapToDouble(s -> s.getSalary() != null ? s.getSalary() : 0).sum();
    }
}
