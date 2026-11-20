
-- ============================================================
-- ASSIGNMENT 5: HOSPITAL PATIENT RECORDS
-- Complete Combined SQL Script
-- Database: MySQL 8.0.16+
-- ============================================================

-- SECTION 1: CREATE THE MAIN DATABASE
-- WARNING: This drops the existing database and its data.

DROP DATABASE IF EXISTS hospital_patient_records;

CREATE DATABASE hospital_patient_records;

USE hospital_patient_records;


-- ============================================================
-- SECTION 2: CREATE TABLES (3NF DATABASE DESIGN)
-- ============================================================

-- TABLE 1: DEPARTMENT

CREATE TABLE Department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100)
);


-- TABLE 2: PATIENT

CREATE TABLE Patient (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    address VARCHAR(255)
);


-- TABLE 3: DOCTOR

CREATE TABLE Doctor (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),

    department_id INT NOT NULL,

    CONSTRAINT fk_doctor_department
        FOREIGN KEY (department_id)
        REFERENCES Department(department_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- TABLE 4: VISIT

CREATE TABLE Visit (
    visit_id INT AUTO_INCREMENT PRIMARY KEY,

    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,

    visit_date DATE NOT NULL,
    diagnosis VARCHAR(255),
    prescription TEXT,

    CONSTRAINT fk_visit_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- TABLE 5: BILLING

CREATE TABLE Billing (
    billing_id INT AUTO_INCREMENT PRIMARY KEY,

    visit_id INT NOT NULL UNIQUE,
    bill_amount DECIMAL(10,2) NOT NULL,
    billing_date DATE NOT NULL,

    payment_status VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    CONSTRAINT fk_billing_visit
        FOREIGN KEY (visit_id)
        REFERENCES Visit(visit_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_bill_amount
        CHECK (bill_amount >= 0),

    CONSTRAINT chk_payment_status
        CHECK (payment_status IN ('OPEN', 'PAID', 'CANCELLED'))
);


-- ============================================================
-- SECTION 3: CREATE TRIGGERS FOR VALID VISIT DATES
-- Prevent future visit dates during INSERT and UPDATE.
-- ============================================================

DELIMITER //

CREATE TRIGGER trg_validate_visit_date_insert
BEFORE INSERT ON Visit
FOR EACH ROW
BEGIN
    IF NEW.visit_date > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Visit date cannot be in the future';
    END IF;
END//


CREATE TRIGGER trg_validate_visit_date_update
BEFORE UPDATE ON Visit
FOR EACH ROW
BEGIN
    IF NEW.visit_date > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Visit date cannot be in the future';
    END IF;
END//

DELIMITER ;


-- ============================================================
-- SECTION 4: DATABASE MANAGEMENT OPERATIONS
-- Create a user-defined database, rename a table,
-- drop the database and recreate it.
-- ============================================================

CREATE DATABASE hospital_user_db;

USE hospital_user_db;

CREATE TABLE old_patient_records (
    record_id INT PRIMARY KEY,
    record_description VARCHAR(100)
);

-- Rename the table

RENAME TABLE old_patient_records TO archived_patient_records;

-- Drop the user-defined database

DROP DATABASE hospital_user_db;

-- Recreate the database

CREATE DATABASE hospital_user_db;


-- Return to the main database

USE hospital_patient_records;


-- ============================================================
-- SECTION 5: INSERT SAMPLE DATA
-- ============================================================

-- INSERT DEPARTMENTS

INSERT INTO Department
(department_name, location)
VALUES
('Cardiology', 'Block A'),
('Neurology', 'Block B'),
('Orthopedics', 'Block C'),
('Dermatology', 'Block D');


-- INSERT PATIENTS

INSERT INTO Patient
(patient_name, date_of_birth, gender, phone, email, address)
VALUES
('Arun Kumar', '1990-05-12', 'Male', '9876543210',
 'arun@gmail.com', 'Coimbatore'),

('Priya Sharma', '1995-08-20', 'Female', '9876543211',
 'priya@gmail.com', 'Chennai'),

('Rahul Verma', '1985-02-15', 'Male', '9876543212',
 'rahul@gmail.com', 'Madurai'),

('Sneha Raj', '2000-11-25', 'Female', '9876543213',
 'sneha@gmail.com', 'Salem'),

('Vikram Singh', '1978-07-10', 'Male', '9876543214',
 'vikram@gmail.com', 'Erode'),

('Meena Devi', '1992-03-18', 'Female', '9876543215',
 'meena@gmail.com', 'Tiruppur');


-- INSERT DOCTORS

INSERT INTO Doctor
(doctor_name, specialization, phone, department_id)
VALUES
('Dr. Rajesh', 'Heart Specialist', '9000000001', 1),
('Dr. Anitha', 'Neurologist', '9000000002', 2),
('Dr. Kumar', 'Orthopedic Surgeon', '9000000003', 3),
('Dr. Divya', 'Skin Specialist', '9000000004', 4);


-- INSERT VISITS

INSERT INTO Visit
(patient_id, doctor_id, visit_date, diagnosis, prescription)
VALUES
(1, 1, '2026-10-05', 'High Blood Pressure',
 'Blood pressure medication'),

(2, 2, '2026-10-08', 'Migraine',
 'Pain relief medication'),

(3, 3, '2026-10-10', 'Knee Pain',
 'Physiotherapy and medication'),

(4, 4, '2026-10-12', 'Skin Allergy',
 'Antihistamine medication'),

(1, 1, '2026-10-20', 'Chest Pain',
 'ECG and further evaluation'),

(5, 3, '2026-11-02', 'Back Pain',
 'Rest and physiotherapy'),

(2, 2, '2026-11-05', 'Headache',
 'Medication and follow-up'),

(3, 1, '2026-11-10', 'Heart Check-up',
 'Regular monitoring');


-- INSERT BILLING RECORDS

INSERT INTO Billing
(visit_id, bill_amount, billing_date, payment_status)
VALUES
(1, 1500.00, '2026-10-05', 'PAID'),
(2, 1200.00, '2026-10-08', 'OPEN'),
(3, 2000.00, '2026-10-10', 'PAID'),
(4, 1000.00, '2026-10-12', 'OPEN'),
(5, 2500.00, '2026-10-20', 'PAID'),
(6, 1800.00, '2026-11-02', 'OPEN'),
(7, 900.00, '2026-11-05', 'PAID'),
(8, 2200.00, '2026-11-10', 'OPEN');


-- ============================================================
-- SECTION 6: UPDATE OPERATIONS
-- ============================================================

-- Update a patient's phone number

UPDATE Patient
SET phone = '9999999999'
WHERE patient_id = 1;


-- Update a billing payment status

UPDATE Billing
SET payment_status = 'PAID'
WHERE billing_id = 2;


-- ============================================================
-- SECTION 7: DELETE OPERATION
-- ============================================================

-- Delete a patient who has no visit records.

DELETE FROM Patient
WHERE patient_id = 6;


-- ============================================================
-- SECTION 8: REQUIRED RETRIEVAL QUERIES
-- ============================================================

-- QUERY 1: Display all patients

SELECT *
FROM Patient;


-- QUERY 2: Display all doctors

SELECT *
FROM Doctor;


-- QUERY 3: Display all departments

SELECT *
FROM Department;


-- QUERY 4: Display all visits

SELECT *
FROM Visit;


-- QUERY 5: Display all billing records

SELECT *
FROM Billing;


-- QUERY 6: Retrieve patients who visited during October 2026

SELECT DISTINCT
    p.patient_id,
    p.patient_name,
    v.visit_date,
    v.diagnosis
FROM Patient p
JOIN Visit v
    ON p.patient_id = v.patient_id
WHERE v.visit_date >= '2026-10-01'
  AND v.visit_date < '2026-11-01';


-- QUERY 7: Retrieve doctors belonging to each department

SELECT
    d.doctor_id,
    d.doctor_name,
    d.specialization,
    dep.department_name
FROM Doctor d
JOIN Department dep
    ON d.department_id = dep.department_id
ORDER BY dep.department_name;


-- QUERY 8: Display patient visit details with doctor information

SELECT
    p.patient_name,
    d.doctor_name,
    dep.department_name,
    v.visit_date,
    v.diagnosis
FROM Visit v
JOIN Patient p
    ON v.patient_id = p.patient_id
JOIN Doctor d
    ON v.doctor_id = d.doctor_id
JOIN Department dep
    ON d.department_id = dep.department_id;


-- QUERY 9: Display billing details with patient names

SELECT
    b.billing_id,
    p.patient_name,
    b.bill_amount,
    b.billing_date,
    b.payment_status
FROM Billing b
JOIN Visit v
    ON b.visit_id = v.visit_id
JOIN Patient p
    ON v.patient_id = p.patient_id;


-- QUERY 10: Display patients with their total visit count

SELECT
    p.patient_id,
    p.patient_name,
    COUNT(v.visit_id) AS total_visits
FROM Patient p
LEFT JOIN Visit v
    ON p.patient_id = v.patient_id
GROUP BY p.patient_id, p.patient_name;


-- QUERY 11: Display total billing amount

SELECT
    SUM(bill_amount) AS total_billing_amount
FROM Billing;


-- QUERY 12: Display unpaid bills

SELECT *
FROM Billing
WHERE payment_status = 'OPEN';


-- QUERY 13: Display doctors from the Cardiology department

SELECT
    d.doctor_id,
    d.doctor_name,
    d.specialization,
    dep.department_name
FROM Doctor d
JOIN Department dep
    ON d.department_id = dep.department_id
WHERE dep.department_name = 'Cardiology';


-- QUERY 14: Display patients who have more than one visit

SELECT
    p.patient_id,
    p.patient_name,
    COUNT(v.visit_id) AS total_visits
FROM Patient p
JOIN Visit v
    ON p.patient_id = v.patient_id
GROUP BY p.patient_id, p.patient_name
HAVING COUNT(v.visit_id) > 1;


-- QUERY 15: Display complete hospital visit and billing details

SELECT
    v.visit_id,
    p.patient_name,
    d.doctor_name,
    dep.department_name,
    v.visit_date,
    v.diagnosis,
    v.prescription,
    b.bill_amount,
    b.payment_status
FROM Visit v
JOIN Patient p
    ON v.patient_id = p.patient_id
JOIN Doctor d
    ON v.doctor_id = d.doctor_id
JOIN Department dep
    ON d.department_id = dep.department_id
LEFT JOIN Billing b
    ON v.visit_id = b.visit_id
ORDER BY v.visit_date;


-- ============================================================
-- SECTION 9: CONSTRAINT VERIFICATION
-- ============================================================

-- Verify the default payment status is OPEN.
-- This test inserts a new billing row without specifying status.
-- The visit must exist and must not already have a billing row.

-- First create an additional visit for testing.

INSERT INTO Visit
(patient_id, doctor_id, visit_date, diagnosis, prescription)
VALUES
(4, 4, '2026-11-15', 'Routine Check-up', 'General advice');


-- Insert billing without payment_status.
-- The default value should automatically become OPEN.

INSERT INTO Billing
(visit_id, bill_amount, billing_date)
VALUES
(LAST_INSERT_ID(), 500.00, '2026-11-15');


-- Verify the default status

SELECT *
FROM Billing
WHERE billing_id = LAST_INSERT_ID();


-- Verify all billing amounts are non-negative

SELECT *
FROM Billing
WHERE bill_amount < 0;


-- Verify all visit dates are not in the future

SELECT *
FROM Visit
WHERE visit_date > CURDATE();


-- ============================================================
-- SECTION 10: FINAL DATABASE VERIFICATION
-- ============================================================

-- Display all tables

SHOW TABLES;


-- Display table structures

DESCRIBE Patient;
DESCRIBE Doctor;
DESCRIBE Department;
DESCRIBE Visit;
DESCRIBE Billing;


-- Display trigger definitions

SHOW TRIGGERS;


-- Final record counts

SELECT 'Patient' AS table_name, COUNT(*) AS total_records
FROM Patient

UNION ALL

SELECT 'Doctor', COUNT(*)
FROM Doctor

UNION ALL

SELECT 'Department', COUNT(*)
FROM Department

UNION ALL

SELECT 'Visit', COUNT(*)
FROM Visit

UNION ALL

SELECT 'Billing', COUNT(*)
FROM Billing;


-- ============================================================
-- END OF ASSIGNMENT 5 SQL SCRIPT
-- ============================================================
