package com.credit_risk_scoring_service.dto;

import com.credit_risk_scoring_service.model.EmploymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreditApplicationRequest {

    @NotNull(message = "Annual income is required")
    @Positive(message = "Annual income must be positive")
    private Double annualIncome;

    @NotNull(message = "Existing Debt Amount is required")
    @Min(0)
    private Double existingDebtAmount;

    @NotNull(message = "Monthly Debt Payment is required")
    @Min(0)
    private Double monthlyDebtPayments;

    @NotNull(message = "Missed Payment is required")
    @Min(0)
    private Integer missedPayments;

    @NotNull(message = "Credit History Month is required")
    @Min(0)
    private Integer creditHistoryMonths;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Requested amount is required")
    @Positive(message = "Requested amount must be positive")
    private Double requestedAmount;
}