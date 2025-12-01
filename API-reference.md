# Employee Salary Management API Reference

Base URL: `http://localhost:8080/api/v1`

## Overview
This document describes the REST endpoints exposed by the Employee Salary Management System. All responses are JSON. Unless stated otherwise, success responses return HTTP 200. Create operations return HTTP 201, delete operations may return HTTP 204.

No authentication headers are defined in the current collection.

## Conventions
- Monetary values are decimals.
- Timestamps use ISO-8601 (e.g. `2024-03-15T14:20:30`).
- Pagination: `page` (0-based) and `size` query params.

---
## 1. Salary Configuration
### Get Salary Configuration
GET `/configuration/salary`
Returns current global salary configuration.

Success 200 Response Example:
```json
{
  "id": 1,
  "baseSalary": 25000.00,
  "gradeIncrement": 5000.00,
  "houseRentPercentage": 20,
  "medicalPercentage": 15,
  "updatedAt": "2024-03-15T10:30:00"
}
```

### Update Salary Configuration
PUT `/configuration/salary`
Updates the salary configuration.

Request Body:
```json
{
  "baseSalary": 30000,
  "gradeIncrement": 6000,
  "houseRentPercentage": 25,
  "medicalPercentage": 20
}
```
Success 200 Response Example:
```json
{
  "id": 1,
  "baseSalary": 30000.00,
  "gradeIncrement": 6000.00,
  "houseRentPercentage": 25,
  "medicalPercentage": 20,
  "updatedAt": "2024-03-15T14:45:00"
}
```

---
## 2. Company Account
### Get Company Account
GET `/company/account`
Fetch company bank account and balance.

Success 200 Response Example:
```json
{
  "id": 1,
  "accountName": "Company Main Account",
  "accountNumber": "COMP-ACC-001",
  "currentBalance": 1000000.00,
  "bankName": "Central Bank",
  "branchName": "Main Branch",
  "updatedAt": "2024-03-15T10:00:00"
}
```

### Top-Up Company Account
POST `/company/account/topup`
Increase the company account balance.

Request Body:
```json
{
  "amount": 500000
}
```
Success 200 Response Example:
```json
{
  "id": 1,
  "accountName": "Company Main Account",
  "accountNumber": "COMP-ACC-001",
  "currentBalance": 1500000.00,
  "bankName": "Central Bank",
  "branchName": "Main Branch",
  "updatedAt": "2024-03-15T11:30:00"
}
```

---
## 3. Employee Management
### Create Employee
POST `/employees`
Creates a new employee with bank account info.

Request Body:
```json
{
  "name": "John Doe",
  "grade": "GRADE_5",
  "address": "123 Main St, Dhaka",
  "mobileNumber": "+8801712345678",
  "bankAccount": {
    "accountName": "John Doe Savings",
    "accountNumber": "1234567890",
    "accountType": "SAVINGS",
    "bankName": "Dhaka Bank",
    "branchName": "Gulshan Branch"
  }
}
```
Success 201 Response Example:
```json
{
  "id": 1,
  "employeeId": "0423",
  "name": "John Doe",
  "grade": "GRADE_5",
  "address": "123 Main St, Dhaka",
  "mobileNumber": "+8801712345678",
  "bankAccountNumber": "1234567890",
  "maskedBankAccountNumber": "******7890",
  "createdAt": "2024-03-15T10:30:00"
}
```

### Get All Employees (Paginated)
GET `/employees?page={page}&size={size}`
Returns paginated list.

Success 200 Response Example:
```json
{
  "content": [
    {
      "id": 1,
      "employeeId": "0423",
      "name": "John Doe",
      "grade": "GRADE_5",
      "address": "123 Main St, Dhaka",
      "mobileNumber": "+8801712345678",
      "bankAccountNumber": "1234567890",
      "maskedBankAccountNumber": "******7890",
      "createdAt": "2024-03-15T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Get Employee by ID
GET `/employees/{employeeId}`
Retrieve single employee.

Success 200 Response Example (employeeId=0423):
```json
{
  "id": 1,
  "employeeId": "0423",
  "name": "John Doe",
  "grade": "GRADE_5",
  "address": "123 Main St, Dhaka",
  "mobileNumber": "+8801712345678",
  "bankAccountNumber": "1234567890",
  "maskedBankAccountNumber": "******7890",
  "createdAt": "2024-03-15T10:30:00"
}
```

### Update Employee
PUT `/employees/{employeeId}`
Updates employee core info.

Request Body:
```json
{
  "name": "John Doe Updated",
  "grade": "GRADE_4",
  "address": "456 New Address, Dhaka",
  "mobileNumber": "+8801712345678"
}
```
Success 200 Response Example:
```json
{
  "id": 1,
  "employeeId": "0423",
  "name": "John Doe Updated",
  "grade": "GRADE_4",
  "address": "456 New Address, Dhaka",
  "mobileNumber": "+8801712345678",
  "bankAccountNumber": "1234567890",
  "maskedBankAccountNumber": "******7890",
  "createdAt": "2024-03-15T10:30:00"
}
```

### Delete Employee
DELETE `/employees/{employeeId}`
Deletes employee record.

Success 204 No Content (no body).

---
## 4. Payroll Processing
### Preview Employee Salary
GET `/payroll/salary/{employeeId}`
Computes salary breakdown without processing payment.

Success 200 Response Example:
```json
{
  "employeeId": "0423",
  "employeeName": "John Doe",
  "grade": "GRADE_5",
  "basic": 30000.00,
  "houseRent": 6000.00,
  "medical": 4500.00,
  "gross": 40500.00,
  "status": null,
  "paidAt": null
}
```

### Process Payroll (All Employees)
POST `/payroll/process`
Processes salaries for all employees.

Success 200 Response Example:
```json
{
  "totalPaid": 121500.00,
  "paidCount": 3,
  "failedCount": 0,
  "remainingCompanyBalance": 878500.00,
  "salarySlips": [
    {
      "employeeId": "0423",
      "employeeName": "John Doe",
      "grade": "GRADE_5",
      "basic": 30000.00,
      "houseRent": 6000.00,
      "medical": 4500.00,
      "gross": 40500.00,
      "status": "PAID",
      "paidAt": "2024-03-15T14:20:30"
    }
  ]
}
```

### Process Payroll (Selected Employees)
POST `/payroll/process?ids={id1},{id2},{id3}`
Processes payroll for selected employee IDs.

Success 200 Response Example:
```json
{
  "totalPaid": 81000.00,
  "paidCount": 2,
  "failedCount": 0,
  "remainingCompanyBalance": 919000.00,
  "salarySlips": [
    {
      "employeeId": "0423",
      "employeeName": "John Doe",
      "grade": "GRADE_5",
      "basic": 30000.00,
      "houseRent": 6000.00,
      "medical": 4500.00,
      "gross": 40500.00,
      "status": "PAID",
      "paidAt": "2024-03-15T14:20:30"
    }
  ]
}
```

### Get Employee Salary History
GET `/payroll/history/{employeeId}`
Returns past paid salary slips for employee.

Success 200 Response Example:
```json
[
  {
    "employeeId": "0423",
    "employeeName": "John Doe",
    "grade": "GRADE_5",
    "basic": 30000.00,
    "houseRent": 6000.00,
    "medical": 4500.00,
    "gross": 40500.00,
    "status": "PAID",
    "paidAt": "2024-03-15T14:20:30"
  },
  {
    "employeeId": "0423",
    "employeeName": "John Doe",
    "grade": "GRADE_5",
    "basic": 30000.00,
    "houseRent": 6000.00,
    "medical": 4500.00,
    "gross": 40500.00,
    "status": "PAID",
    "paidAt": "2024-02-15T14:20:30"
  }
]
```

---
## 5. Reports
### Get Salary Sheet by Date
GET `/reports/salary-sheet?date={YYYY-MM-DD}`
Returns salary slips paid on specific date.

Success 200 Response Example:
```json
[
  {
    "employeeId": "0423",
    "employeeName": "John Doe",
    "grade": "GRADE_5",
    "basic": 30000.00,
    "houseRent": 6000.00,
    "medical": 4500.00,
    "gross": 40500.00,
    "status": "PAID",
    "paidAt": "2024-03-15T14:20:30"
  },
  {
    "employeeId": "0856",
    "employeeName": "Jane Smith",
    "grade": "GRADE_4",
    "basic": 35000.00,
    "houseRent": 7000.00,
    "medical": 5250.00,
    "gross": 47250.00,
    "status": "PAID",
    "paidAt": "2024-03-15T14:21:00"
  }
]
```

### Get Summary Report by Date Range
GET `/reports/summary?from={YYYY-MM-DD}&to={YYYY-MM-DD}`
Aggregated salary payment summary in date range.

Success 200 Response Example:
```json
{
  "totalPaid": 364500.00,
  "paidCount": 9,
  "failedCount": 0,
  "remainingCompanyBalance": 635500.00,
  "salarySlips": [
    {
      "employeeId": "0423",
      "employeeName": "John Doe",
      "grade": "GRADE_5",
      "basic": 30000.00,
      "houseRent": 6000.00,
      "medical": 4500.00,
      "gross": 40500.00,
      "status": "PAID",
      "paidAt": "2024-03-15T14:20:30"
    }
  ]
}
```

---
## 6. Error Scenarios
### Insufficient Balance
POST `/payroll/process`
Occurs when company account balance is lower than required total.

Error 409 Response Example:
```json
{
  "timestamp": "2024-03-15T14:30:00",
  "status": 409,
  "error": "Insufficient Balance",
  "message": "Insufficient balance. Required: 40500.00, Available: 20000.00, Top-up needed: 20500.00",
  "path": "/api/v1/payroll/process",
  "requiredAmount": 40500.00,
  "availableBalance": 20000.00,
  "requiredTopUp": 20500.00
}
```

### Employee Not Found
GET `/employees/{employeeId}`
When requested employee ID does not exist.

Error 404 Response Example:
```json
{
  "timestamp": "2024-03-15T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with ID: 9999",
  "path": "/api/v1/employees/9999"
}
```

### Validation Error (Create Employee)
POST `/employees`
Triggered by invalid or missing fields.

Error 400 Response Example:
```json
{
  "timestamp": "2024-03-15T14:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input parameters",
  "path": "/api/v1/employees",
  "validationErrors": {
    "name": "Name is required",
    "address": "Address is required",
    "mobileNumber": "Invalid mobile number format",
    "bankAccount.accountName": "Account name is required",
    "bankAccount.accountNumber": "Account number is required"
  }
}
```

---
## Status Codes Summary
- 200 OK: Successful retrieval or update
- 201 Created: Resource successfully created
- 204 No Content: Successful deletion
- 400 Bad Request: Validation failure
- 404 Not Found: Resource not found
- 409 Conflict: Business rule violation (e.g., insufficient balance)

## Future Enhancements (Suggested)
- Authentication & Authorization section (e.g., JWT bearer tokens)
- Standard error wrapper description
- Model schemas with field constraints
- Filtering & sorting for employee list

## Changelog
- v1.0 Initial draft derived from Postman collection.

