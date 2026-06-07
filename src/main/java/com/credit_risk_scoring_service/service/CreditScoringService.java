package com.credit_risk_scoring_service.service;

import com.credit_risk_scoring_service.dto.CreditApplicationRequest;
import com.credit_risk_scoring_service.model.CreditApplication;
import com.credit_risk_scoring_service.model.CreditDecision;
import com.credit_risk_scoring_service.model.EmploymentStatus;
import com.credit_risk_scoring_service.repository.CreditApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditScoringService {

    private final CreditApplicationRepository repository;

    public CreditScoringService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    public CreditApplication evaluate(CreditApplicationRequest request) {

        int score = 500; // base score
        List<String> reasons = new ArrayList<>();

        // Rule 1: Debt-to-income ratio
        double monthlyIncome = request.getAnnualIncome() / 12;
        double dti = request.getMonthlyDebtPayments() / monthlyIncome;

        if (dti < 0.2) {
            score += 100;
            reasons.add("Low debt-to-income ratio: strong");
        } else if (dti < 0.4) {
            score += 40;
            reasons.add("Moderate debt-to-income ratio: acceptable");
        } else {
            score -= 100;
            reasons.add("High debt-to-income ratio: risk factor");
        }

        // Rule 2: Missed payments
        if (request.getMissedPayments() == 0) {
            score += 80;
            reasons.add("No missed payments: positive history");
        } else if (request.getMissedPayments() <= 2) {
            score -= 40;
            reasons.add("Some missed payments: minor risk");
        } else {
            score -= 120;
            reasons.add("Multiple missed payments: significant risk");
        }

        // Rule 3: Credit history length
        if (request.getCreditHistoryMonths() >= 36) {
            score += 60;
            reasons.add("Established credit history: positive");
        } else if (request.getCreditHistoryMonths() >= 12) {
            score += 20;
            reasons.add("Limited credit history: neutral");
        } else {
            score -= 40;
            reasons.add("Very short credit history: risk factor");
        }

        // Rule 4: Employment status
        if (request.getEmploymentStatus() == EmploymentStatus.EMPLOYED) {
            score += 60;
            reasons.add("Employed: stable income signal");
        } else if (request.getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            score += 20;
            reasons.add("Self-employed: variable income noted");
        } else {
            score -= 100;
            reasons.add("Unemployed: significant risk factor");
        }

        // Rule 5: Loan-to-income ratio
        double lti = request.getRequestedAmount() / request.getAnnualIncome();
        if (lti < 0.3) {
            score += 40;
            reasons.add("Requested amount low relative to income: positive");
        } else if (lti < 0.5) {
            reasons.add("Requested amount moderate relative to income: neutral");
        } else {
            score -= 60;
            reasons.add("Requested amount high relative to income: risk factor");
        }

        // Map score to decision
        CreditDecision decision;
        if (score >= 650) {
            decision = CreditDecision.APPROVE;
        } else if (score >= 500) {
            decision = CreditDecision.REVIEW;
        } else {
            decision = CreditDecision.DECLINE;
        }

        // Build and persist the entity
        CreditApplication application = new CreditApplication();
        application.setAnnualIncome(request.getAnnualIncome());
        application.setExistingDebtAmount(request.getExistingDebtAmount());
        application.setMonthlyDebtPayments(request.getMonthlyDebtPayments());
        application.setMissedPayments(request.getMissedPayments());
        application.setCreditHistoryMonths(request.getCreditHistoryMonths());
        application.setEmploymentStatus(request.getEmploymentStatus());
        application.setRequestedAmount(request.getRequestedAmount());
        application.setCreditScore(score);
        application.setDecision(decision);
        application.setDecisionReasons(String.join(" | ", reasons));

        return repository.save(application);
    }

    public CreditApplication getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));
    }
}