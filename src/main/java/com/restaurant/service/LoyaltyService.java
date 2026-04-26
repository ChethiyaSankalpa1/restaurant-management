package com.restaurant.service;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyProgramRepository loyaltyRepo;
    private final CustomerRepository customerRepository;

    // Default: 1 point per $1 spent
    private static final int POINTS_PER_DOLLAR = 1;
    private static final int BRONZE_THRESHOLD = 0;
    private static final int SILVER_THRESHOLD = 500;
    private static final int GOLD_THRESHOLD = 2000;

    public void awardPoints(Order order) {
        if (order.getCustomerId() == null || order.getTotalAmount() == null) return;

        int pointsToAward = (int) (order.getTotalAmount() * POINTS_PER_DOLLAR);
        if (pointsToAward <= 0) return;

        LoyaltyProgram lp = loyaltyRepo.findByCustomerId(order.getCustomerId())
            .orElse(LoyaltyProgram.builder()
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .points(0)
                .tier("Bronze")
                .transactions(new java.util.ArrayList<>())
                .build());

        lp.setPoints((lp.getPoints() != null ? lp.getPoints() : 0) + pointsToAward);
        lp.setTier(calculateTier(lp.getPoints()));

        LoyaltyProgram.LoyaltyTransaction tx = LoyaltyProgram.LoyaltyTransaction.builder()
            .type("EARNED")
            .points(pointsToAward)
            .orderId(order.getId())
            .description("Points earned from order " + order.getOrderNumber())
            .createdAt(LocalDateTime.now())
            .build();
        if (lp.getTransactions() == null) lp.setTransactions(new java.util.ArrayList<>());
        lp.getTransactions().add(tx);

        loyaltyRepo.save(lp);

        // Update customer loyalty points
        customerRepository.findById(order.getCustomerId()).ifPresent(customer -> {
            customer.setLoyaltyPoints((customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0) + pointsToAward);
            customerRepository.save(customer);
        });
    }

    public double redeemPoints(String customerId, int pointsToRedeem) {
        LoyaltyProgram lp = loyaltyRepo.findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("No loyalty account found"));

        if (lp.getPoints() == null || lp.getPoints() < pointsToRedeem) {
            throw new RuntimeException("Insufficient points");
        }

        lp.setPoints(lp.getPoints() - pointsToRedeem);
        lp.setTier(calculateTier(lp.getPoints()));

        LoyaltyProgram.LoyaltyTransaction tx = LoyaltyProgram.LoyaltyTransaction.builder()
            .type("REDEEMED")
            .points(-pointsToRedeem)
            .description("Points redeemed as discount")
            .createdAt(LocalDateTime.now())
            .build();
        if (lp.getTransactions() == null) lp.setTransactions(new java.util.ArrayList<>());
        lp.getTransactions().add(tx);

        loyaltyRepo.save(lp);

        customerRepository.findById(customerId).ifPresent(c -> {
            c.setLoyaltyPoints(Math.max(0, (c.getLoyaltyPoints() != null ? c.getLoyaltyPoints() : 0) - pointsToRedeem));
            customerRepository.save(c);
        });

        // 1 point = $0.10 discount
        return pointsToRedeem * 0.10;
    }

    private String calculateTier(int points) {
        if (points >= GOLD_THRESHOLD) return "Gold";
        if (points >= SILVER_THRESHOLD) return "Silver";
        return "Bronze";
    }

    public List<LoyaltyProgram> getAllLoyaltyAccounts() { return loyaltyRepo.findAll(); }
    public Optional<LoyaltyProgram> findByCustomerId(String id) { return loyaltyRepo.findByCustomerId(id); }
}
