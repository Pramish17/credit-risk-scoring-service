package com.credit_risk_scoring_service.dto;

import com.credit_risk_scoring_service.model.EmploymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreditApplicationRequest {

    @NotNull
    @Positive
    private Double annualIncome;

    @NotNull
    @Min(0)
    private Double existingDebtAmount;

    @NotNull
    @Min(0)
    private Double monthlyDebtPayments;

    @NotNull
    @Min(0)
    private Integer missedPayments;

    @NotNull
    @Min(0)
    private Integer creditHistoryMonths;

    @NotNull
    private EmploymentStatus employmentStatus;

    @NotNull
    @Positive
    private Double requestedAmount;
}