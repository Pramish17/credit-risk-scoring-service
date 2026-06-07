package com.credit_risk_scoring_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class CreditApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Input fields (copied from the request)
    private Double annualIncome;
    private Double existingDebtAmount;
    private Double monthlyDebtPayments;
    private Integer missedPayments;
    private Integer creditHistoryMonths;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    private Double requestedAmount;
    // Server-generated fields (never sent by client)
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    private CreditDecision decision;

    private String decisionReasons;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
