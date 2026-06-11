# Credit Risk Scoring Service

A production-style Java/Spring Boot microservice that evaluates credit applications and returns a scored decision with explainable reasons.

Built to demonstrate real-world fintech backend engineering: input validation, a transparent rules engine, immutable audit records, and explainable decisions — the same concerns I worked on during 1.5 years on Equifax's credit risk and identity verification platform at Oncore.

---

## Why explainability matters

In consumer lending, a credit decision is not just a yes or a no. Applicants can challenge decisions, regulators can audit them, and underwriters need to act on them. Every decision this service returns includes the specific factors that drove it — not just the outcome. This is a legal and operational requirement in regulated finance, not a nice-to-have.

---

## How it works

Each application is scored from a base of 500 points. Five rules adjust the score up or down based on the applicant's financial signals. The final score maps to a decision band.

| Rule | Signal | Impact |
|---|---|---|
| Debt-to-income ratio (DTI) | Monthly debt / monthly income | +100 (low), +40 (moderate), -100 (high) |
| Missed payments | Count of missed payments | +80 (none), -40 (1-2), -120 (3+) |
| Credit history length | Months of credit history | +60 (36m+), +20 (12-35m), -40 (under 12m) |
| Employment status | EMPLOYED / SELF_EMPLOYED / UNEMPLOYED | +60 / +20 / -100 |
| Loan-to-income ratio (LTI) | Requested amount / annual income | +40 (low), 0 (moderate), -60 (high) |

**Decision bands:**
- 650 and above: APPROVE
- 500 to 649: REVIEW
- Below 500: DECLINE

Every response includes the factors that contributed to the decision, pipe-separated, so any decision can be explained and audited.

---

## Tech stack

- Java 17, Spring Boot 3
- Spring Data JPA, Hibernate
- Bean Validation (Jakarta)
- H2 (in-memory, local dev)
- Lombok
- Maven

---

## Project structure

```
src/main/java/com/credit_risk_scoring_service/
├── controller/    CreditApplicationController
├── dto/           CreditApplicationRequest
├── model/         CreditApplication, CreditDecision, EmploymentStatus
├── repository/    CreditApplicationRepository
└── service/       CreditScoringService
```

---

## Running locally

```bash
git clone https://github.com/Pramish17/credit-risk-scoring-service.git
cd credit-risk-scoring-service
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`. H2 console available at `http://localhost:8080/h2-console`.

---

## API reference

### POST /applications
Submit a credit application. Returns a scored decision with reasons.

**Request:**
```json
{
    "annualIncome": 50000.0,
    "existingDebtAmount": 5000.0,
    "monthlyDebtPayments": 300.0,
    "missedPayments": 0,
    "creditHistoryMonths": 48,
    "employmentStatus": "EMPLOYED",
    "requestedAmount": 10000.0
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "annualIncome": 50000.0,
    "existingDebtAmount": 5000.0,
    "monthlyDebtPayments": 300.0,
    "missedPayments": 0,
    "creditHistoryMonths": 48,
    "employmentStatus": "EMPLOYED",
    "requestedAmount": 10000.0,
    "creditScore": 840,
    "decision": "APPROVE",
    "decisionReasons": "Low debt-to-income ratio: strong | No missed payments: positive history | Established credit history: positive | Employed: stable income signal | Requested amount low relative to income: positive",
    "createdAt": "2026-06-07T16:30:09.747003"
}
```

### GET /applications/{id}
Retrieve a previously scored application by ID. Supports audit and review workflows.

**Response (200 OK):** same structure as above.

---

## High-risk example

```json
{
    "annualIncome": 25000.0,
    "existingDebtAmount": 20000.0,
    "monthlyDebtPayments": 1200.0,
    "missedPayments": 5,
    "creditHistoryMonths": 6,
    "employmentStatus": "UNEMPLOYED",
    "requestedAmount": 20000.0
}
```

Returns score 80, decision DECLINE, with all contributing factors listed in `decisionReasons`.

---

## Planned extensions

- **JUnit / Mockito test suite** targeting 95%+ coverage on the scoring rules engine, consistent with production quality standards from my Equifax work.
- **Production database** via Cloud SQL (GCP) or RDS (AWS), replacing the H2 in-memory store.
- **JWT authentication** using Spring Security to secure the API endpoints.
- **ML scoring model** as an alternative to the rules engine: a Random Forest model (consistent with my MSc dissertation work) could replace or complement the rules, while the explainability layer remains, since regulators require it regardless of the model type.
- **Bean validation messages:** field-level validation error messages added to all DTO fields for human-readable 400 responses.
---

## Author

Pramish Thapa — Backend Software Engineer  
[LinkedIn](https://linkedin.com/in/pramish-thapa-36b828156) | [GitHub](https://github.com/Pramish17) | [Portfolio](https://pramishthapa.com)