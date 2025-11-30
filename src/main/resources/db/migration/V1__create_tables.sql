-- Create enum types
CREATE TYPE account_type AS ENUM ('SAVINGS', 'CURRENT');
CREATE TYPE employee_grade AS ENUM ('GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5', 'GRADE_6');
CREATE TYPE salary_status AS ENUM ('PAID', 'FAILED');

-- Bank Account Table
CREATE TABLE bank_account (
    id BIGSERIAL PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_type account_type NOT NULL,
    current_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    bank_name VARCHAR(255) NOT NULL,
    branch_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Employee Table
CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    employee_id VARCHAR(4) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    grade employee_grade NOT NULL,
    address TEXT NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    bank_account_id BIGINT UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_employee_bank_account FOREIGN KEY (bank_account_id)
        REFERENCES bank_account(id) ON DELETE SET NULL
);

-- Company Account Table
CREATE TABLE company_account (
    id BIGSERIAL PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    current_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    bank_name VARCHAR(255) NOT NULL,
    branch_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Salary Record Table
CREATE TABLE salary_record (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    basic_salary DECIMAL(15, 2) NOT NULL,
    house_rent DECIMAL(15, 2) NOT NULL,
    medical_allowance DECIMAL(15, 2) NOT NULL,
    gross_salary DECIMAL(15, 2) NOT NULL,
    status salary_status NOT NULL,
    paid_at TIMESTAMP,
    company_balance_before DECIMAL(15, 2) NOT NULL,
    company_balance_after DECIMAL(15, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_salary_record_employee FOREIGN KEY (employee_id)
        REFERENCES employee(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_employee_employee_id ON employee(employee_id);
CREATE INDEX idx_employee_grade ON employee(grade);
CREATE INDEX idx_salary_record_employee_id ON salary_record(employee_id);
CREATE INDEX idx_salary_record_status ON salary_record(status);
CREATE INDEX idx_salary_record_paid_at ON salary_record(paid_at);

-- Insert default company account
INSERT INTO company_account (account_name, account_number, current_balance, bank_name, branch_name)
VALUES ('Company Main Account', 'COMP-ACC-001', 1000000.00, 'Central Bank', 'Main Branch');

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_bank_account_updated_at BEFORE UPDATE ON bank_account
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_employee_updated_at BEFORE UPDATE ON employee
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_company_account_updated_at BEFORE UPDATE ON company_account
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Salary Configuration Table
CREATE TABLE salary_configuration (
    id BIGSERIAL PRIMARY KEY,
    base_salary DECIMAL(15, 2) NOT NULL,
    grade_increment DECIMAL(15, 2) NOT NULL,
    house_rent_percentage INTEGER NOT NULL,
    medical_percentage INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert default salary configuration
INSERT INTO salary_configuration (base_salary, grade_increment, house_rent_percentage, medical_percentage)
VALUES (25000.00, 5000.00, 20, 15);

CREATE TRIGGER update_salary_configuration_updated_at BEFORE UPDATE ON salary_configuration
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();