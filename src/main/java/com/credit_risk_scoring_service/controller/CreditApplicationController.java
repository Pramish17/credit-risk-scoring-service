package com.credit_risk_scoring_service.controller;

import com.credit_risk_scoring_service.dto.CreditApplicationRequest;
import com.credit_risk_scoring_service.model.CreditApplication;
import com.credit_risk_scoring_service.service.CreditScoringService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class CreditApplicationController {

    private final CreditScoringService service;

    public CreditApplicationController(CreditScoringService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreditApplication> submit(
            @Valid @RequestBody CreditApplicationRequest request) {
        CreditApplication result = service.evaluate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplication> getById(@PathVariable Long id) {
        CreditApplication result = service.getById(id);
        return ResponseEntity.ok(result);
    }
}