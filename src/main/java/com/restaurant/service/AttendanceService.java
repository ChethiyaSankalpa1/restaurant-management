package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StaffRepository staffRepository;

    public List<Attendance> getAllAttendance() { return attendanceRepository.findAll(); }
    public Optional<Attendance> findById(String id) { return attendanceRepository.findById(id); }
    public Attendance save(Attendance a) { return attendanceRepository.save(a); }

    public Attendance checkIn(String employeeId) {
        Staff emp = staffRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        LocalDate today = LocalDate.now();
        Optional<Attendance> existing = attendanceRepository.findByEmployeeIdAndDate(employeeId, today);
        if (existing.isPresent()) return existing.get();

        LocalTime checkInTime = LocalTime.now();
        String status = checkInTime.isAfter(LocalTime.of(9, 15)) ? "Late" : "Present";

        Attendance att = Attendance.builder()
            .employeeId(employeeId)
            .employeeName(emp.getName())
            .date(today)
            .checkIn(checkInTime)
            .status(status)
            .branch(emp.getBranch())
            .build();
        return attendanceRepository.save(att);
    }

    public Attendance checkOut(String employeeId) {
        LocalDate today = LocalDate.now();
        Attendance att = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
            .orElseThrow(() -> new RuntimeException("No check-in found for today"));
        att.setCheckOut(LocalTime.now());
        if (att.getCheckIn() != null) {
            double hours = (double) java.time.Duration.between(att.getCheckIn(), att.getCheckOut()).toMinutes() / 60;
            att.setHours(Math.round(hours * 100.0) / 100.0);
        }
        return attendanceRepository.save(att);
    }

    public List<Attendance> getMonthlyAttendance(String employeeId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, start, end);
    }
}
