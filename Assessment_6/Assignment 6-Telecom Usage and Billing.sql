CREATE DATABASE telecom_billing_db;
USE telecom_billing_db;
CREATE TABLE plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(100) NOT NULL,
    monthly_rental DECIMAL(10,2) NOT NULL,
    data_limit_gb DECIMAL(10,2) NOT NULL,
    voice_limit_minutes INT NOT NULL
);
CREATE TABLE subscribers (
    subscriber_id INT AUTO_INCREMENT PRIMARY KEY,
    subscriber_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    circle VARCHAR(50) NOT NULL,
    plan_id INT,

    CONSTRAINT fk_subscriber_plan
    FOREIGN KEY (plan_id)
    REFERENCES plans(plan_id)
);
CREATE TABLE call_records (
    call_id INT AUTO_INCREMENT PRIMARY KEY,
    subscriber_id INT NOT NULL,
    call_date DATE NOT NULL,
    call_minutes INT NOT NULL DEFAULT 0,
    data_used_gb DECIMAL(10,3) NOT NULL DEFAULT 0.000,

    CONSTRAINT fk_call_subscriber
    FOREIGN KEY (subscriber_id)
    REFERENCES subscribers(subscriber_id)
);
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    subscriber_id INT NOT NULL,
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    paid_date DATE,
    amount DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_invoice_subscriber
    FOREIGN KEY (subscriber_id)
    REFERENCES subscribers(subscriber_id)
);

INSERT INTO plans
(plan_id, plan_name, monthly_rental, data_limit_gb, voice_limit_minutes)
VALUES
(1, 'Basic Plan', 299.00, 5.00, 300),
(2, 'Smart Plan', 499.00, 10.00, 1000),
(3, 'Premium Plan', 799.00, 20.00, 2000),
(4, 'Unlimited Plan', 999.00, 50.00, 5000);


-- INSERT DATA INTO SUBSCRIBERS

INSERT INTO subscribers
(subscriber_id, subscriber_name, phone_number, circle, plan_id)
VALUES
(1, 'Arjun Kumar', '9876500001', 'Chennai', 1),
(2, 'Priya Sharma', '9876500002', 'Chennai', 2),
(3, 'Karthik Raj', '9876500003', 'Madurai', 1),
(4, 'Divya Lakshmi', '9876500004', 'Coimbatore', 3),
(5, 'Rahul Kumar', '9876500005', 'Coimbatore', 2),
(6, 'Meena Devi', '9876500006', 'Madurai', 4),
(7, 'Sanjay Kumar', '9876500007', 'Chennai', 3),
(8, 'Anitha Rani', '9876500008', 'Trichy', 1),
(9, 'Vignesh Raj', '9876500009', 'Madurai', 2),
(10, 'Kavya Priya', '9876500010', 'Coimbatore', 4),
(11, 'Naveen Kumar', '9876500011', 'Chennai', 2),
(12, 'Lakshmi Devi', '9876500012', 'Trichy', 3);


-- INSERT DATA INTO CALL_RECORDS

INSERT INTO call_records
(call_id, subscriber_id, call_date, call_minutes, data_used_gb)
VALUES

-- SUBSCRIBER 1
(1, 1, '2026-10-05', 120, 3.200),
(2, 1, '2026-10-20', 100, 2.500),
(3, 1, '2026-11-05', 80, 1.500),

-- SUBSCRIBER 2
(4, 2, '2026-10-08', 200, 6.000),
(5, 2, '2026-10-22', 180, 5.000),
(6, 2, '2026-11-08', 150, 4.000),

-- SUBSCRIBER 3
(7, 3, '2026-10-10', 90, 2.500),
(8, 3, '2026-10-25', 70, 1.500),
(9, 3, '2026-11-10', 110, 5.500),

-- SUBSCRIBER 4
(10, 4, '2026-10-05', 250, 12.500),
(11, 4, '2026-10-25', 200, 9.000),
(12, 4, '2026-11-10', 180, 8.000),

-- SUBSCRIBER 5
(13, 5, '2026-10-07', 160, 4.500),
(14, 5, '2026-10-27', 140, 3.000),
(15, 5, '2026-11-12', 130, 9.000),

-- SUBSCRIBER 6
(16, 6, '2026-10-09', 400, 30.000),
(17, 6, '2026-10-28', 350, 25.000),
(18, 6, '2026-11-12', 300, 12.000),

-- SUBSCRIBER 7
(19, 7, '2026-10-11', 220, 15.000),
(20, 7, '2026-10-29', 190, 7.000),
(21, 7, '2026-11-13', 160, 6.000),

-- SUBSCRIBER 8
(22, 8, '2026-10-15', 100, 5.500),
(23, 8, '2026-11-08', 90, 2.000),

-- SUBSCRIBER 9
(24, 9, '2026-10-06', 170, 7.000),
(25, 9, '2026-10-26', 160, 4.000),
(26, 9, '2026-11-14', 120, 3.000),

-- SUBSCRIBER 10
(27, 10, '2026-10-12', 300, 20.000),
(28, 10, '2026-10-30', 280, 15.000),
(29, 10, '2026-11-15', 250, 45.000),

-- SUBSCRIBER 11
(30, 11, '2026-10-14', 180, 6.000),
(31, 11, '2026-10-28', 150, 5.000),
(32, 11, '2026-11-15', 130, 2.000);

-- SUBSCRIBER 12 INTENTIONALLY HAS NO CALL RECORDS
-- THIS IS REQUIRED TO DEMONSTRATE NULL HANDLING.


-- INSERT DATA INTO INVOICES

INSERT INTO invoices
(invoice_id, subscriber_id, invoice_date, due_date, paid_date, amount)
VALUES

(1, 1, '2026-09-01', '2026-09-15', NULL, 299.00),
(2, 2, '2026-10-01', '2026-10-15', '2026-10-14', 499.00),
(3, 3, '2026-10-05', '2026-10-20', NULL, 299.00),
(4, 4, '2026-10-10', '2026-10-25', '2026-10-24', 799.00),
(5, 5, '2026-10-15', '2026-10-30', NULL, 499.00),
(6, 6, '2026-11-01', '2026-11-15', NULL, 999.00),
(7, 7, '2026-09-01', '2026-09-15', '2026-09-12', 799.00),
(8, 8, '2026-11-05', '2026-11-20', NULL, 299.00),
(9, 9, '2026-10-01', '2026-10-16', NULL, 499.00),
(10, 10, '2026-11-01', '2026-11-30', NULL, 999.00),
(11, 11, '2026-10-20', '2026-11-05', NULL, 499.00),
(12, 1, '2026-10-01', '2026-10-15', NULL, 299.00),
(13, 2, '2026-11-01', '2026-11-15', '2026-11-14', 499.00),
(14, 4, '2026-11-01', '2026-11-15', NULL, 799.00),
(15, 7, '2026-11-01', '2026-11-15', NULL, 799.00),
(16, 3, '2026-11-01', '2026-11-15', NULL, 299.00),
(17, 5, '2026-09-01', '2026-09-15', '2026-09-14', 499.00),
(18, 8, '2026-10-01', '2026-10-15', '2026-10-14', 299.00),
(19, 9, '2026-11-01', '2026-11-15', NULL, 499.00),
(20, 10, '2026-10-01', '2026-10-15', '2026-10-12', 999.00);


-- ============================================================
-- PART 4: VERIFY INSERTED DATA
-- ============================================================

SELECT * FROM plans;

SELECT * FROM subscribers;

SELECT * FROM call_records;

SELECT * FROM invoices;


-- ============================================================
-- TASK 1: EIGHT BUSINESS QUERIES
-- ============================================================


-- QUERY 1: TOTAL CALL MINUTES PER PLAN

SELECT
    p.plan_id,
    p.plan_name,
    COALESCE(SUM(cr.call_minutes), 0) AS total_call_minutes
FROM plans p
LEFT JOIN subscribers s
    ON p.plan_id = s.plan_id
LEFT JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
GROUP BY
    p.plan_id,
    p.plan_name
ORDER BY total_call_minutes DESC;


-- QUERY 2: SUBSCRIBERS EXCEEDING THEIR MONTHLY DATA LIMIT

SELECT
    s.subscriber_id,
    s.subscriber_name,
    p.plan_name,
    p.data_limit_gb,
    DATE_FORMAT(cr.call_date, '%Y-%m') AS usage_month,
    SUM(cr.data_used_gb) AS total_data_used_gb
FROM subscribers s
INNER JOIN plans p
    ON s.plan_id = p.plan_id
INNER JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
GROUP BY
    s.subscriber_id,
    s.subscriber_name,
    p.plan_name,
    p.data_limit_gb,
    DATE_FORMAT(cr.call_date, '%Y-%m')
HAVING
    SUM(cr.data_used_gb) > p.data_limit_gb
ORDER BY total_data_used_gb DESC;


-- QUERY 3: MONTH-WISE REVENUE

SELECT
    DATE_FORMAT(invoice_date, '%Y-%m') AS revenue_month,
    SUM(amount) AS total_revenue
FROM invoices
GROUP BY
    DATE_FORMAT(invoice_date, '%Y-%m')
ORDER BY revenue_month;


-- QUERY 4: INVOICES OVERDUE BY MORE THAN 15 DAYS

SELECT
    invoice_id,
    subscriber_id,
    invoice_date,
    due_date,
    amount,
    DATEDIFF(CURDATE(), due_date) AS overdue_days
FROM invoices
WHERE
    paid_date IS NULL
    AND DATEDIFF(CURDATE(), due_date) > 15
ORDER BY overdue_days DESC;


-- QUERY 5: SUBSCRIBERS WITH NO USAGE

SELECT
    s.subscriber_id,
    s.subscriber_name,
    s.phone_number,
    s.circle
FROM subscribers s
LEFT JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
WHERE cr.call_id IS NULL
ORDER BY s.subscriber_id;


-- QUERY 6: ARPU BY CIRCLE

SELECT
    s.circle,
    COUNT(DISTINCT s.subscriber_id) AS total_subscribers,
    COALESCE(SUM(i.amount), 0) AS total_revenue,
    ROUND(
        COALESCE(SUM(i.amount), 0) /
        NULLIF(COUNT(DISTINCT s.subscriber_id), 0),
        2
    ) AS arpu
FROM subscribers s
LEFT JOIN invoices i
    ON s.subscriber_id = i.subscriber_id
GROUP BY
    s.circle
ORDER BY arpu DESC;


-- QUERY 7: PLAN POPULARITY

SELECT
    p.plan_id,
    p.plan_name,
    COUNT(s.subscriber_id) AS total_subscribers
FROM plans p
LEFT JOIN subscribers s
    ON p.plan_id = s.plan_id
GROUP BY
    p.plan_id,
    p.plan_name
ORDER BY total_subscribers DESC;


-- QUERY 8: TOP 10 DATA CONSUMERS

SELECT
    s.subscriber_id,
    s.subscriber_name,
    s.circle,
    SUM(cr.data_used_gb) AS total_data_consumed_gb
FROM subscribers s
INNER JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
GROUP BY
    s.subscriber_id,
    s.subscriber_name,
    s.circle
ORDER BY total_data_consumed_gb DESC
LIMIT 10;


-- ============================================================
-- TASK 2: STRING AND DATE FUNCTIONS
-- ============================================================


-- QUERY 9: FORMAT INVOICE NUMBERS

SELECT
    invoice_id,
    CONCAT('INV-', LPAD(invoice_id, 8, '0'))
        AS formatted_invoice_number,
    subscriber_id,
    amount
FROM invoices;


-- QUERY 10: CALCULATE INVOICE DUE-DATE STATUS

SELECT
    invoice_id,

    CONCAT('INV-', LPAD(invoice_id, 8, '0'))
        AS formatted_invoice_number,

    invoice_date,
    due_date,
    paid_date,
    amount,

    CASE
        WHEN paid_date IS NOT NULL
            THEN 'PAID'

        WHEN DATEDIFF(CURDATE(), due_date) > 15
            THEN 'OVERDUE MORE THAN 15 DAYS'

        WHEN DATEDIFF(CURDATE(), due_date) > 0
            THEN 'OVERDUE'

        WHEN DATEDIFF(CURDATE(), due_date) = 0
            THEN 'DUE TODAY'

        ELSE 'NOT YET DUE'
    END AS due_date_status,

    DATEDIFF(CURDATE(), due_date) AS days_difference

FROM invoices
ORDER BY invoice_id;


-- ============================================================
-- TASK 3: EFFECT OF NULL ON SUM AND AVG
-- ============================================================


-- QUERY 11: SUM AND AVG WITHOUT NULL HANDLING

SELECT
    s.subscriber_id,
    s.subscriber_name,

    SUM(cr.data_used_gb)
        AS total_data_without_null_handling,

    AVG(cr.data_used_gb)
        AS average_data_without_null_handling,

    SUM(cr.call_minutes)
        AS total_minutes_without_null_handling,

    AVG(cr.call_minutes)
        AS average_minutes_without_null_handling

FROM subscribers s
LEFT JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
GROUP BY
    s.subscriber_id,
    s.subscriber_name
ORDER BY s.subscriber_id;


-- QUERY 12: NULL HANDLING USING IFNULL AND COALESCE

SELECT
    s.subscriber_id,
    s.subscriber_name,

    IFNULL(SUM(cr.data_used_gb), 0)
        AS total_data_used_gb,

    COALESCE(AVG(cr.data_used_gb), 0)
        AS average_data_used_gb,

    IFNULL(SUM(cr.call_minutes), 0)
        AS total_call_minutes,

    COALESCE(AVG(cr.call_minutes), 0)
        AS average_call_minutes

FROM subscribers s
LEFT JOIN call_records cr
    ON s.subscriber_id = cr.subscriber_id
GROUP BY
    s.subscriber_id,
    s.subscriber_name
ORDER BY s.subscriber_id;


-- ============================================================
-- TASK 4: REVENUE PER PLAN AND CIRCLE
-- JOIN + GROUP BY + AGGREGATE FUNCTION
-- ============================================================


-- QUERY 13: REVENUE PER PLAN AND CIRCLE

SELECT
    p.plan_id,
    p.plan_name,
    s.circle,

    COUNT(DISTINCT s.subscriber_id)
        AS total_subscribers,

    COALESCE(SUM(i.amount), 0)
        AS total_revenue,

    ROUND(COALESCE(SUM(i.amount), 0), 2)
        AS revenue_amount

FROM plans p

INNER JOIN subscribers s
    ON p.plan_id = s.plan_id

LEFT JOIN invoices i
    ON s.subscriber_id = i.subscriber_id

GROUP BY
    p.plan_id,
    p.plan_name,
    s.circle

ORDER BY
    p.plan_name,
    s.circle;


SELECT COUNT(*) AS total_plans FROM plans;

SELECT COUNT(*) AS total_subscribers FROM subscribers;

SELECT COUNT(*) AS total_call_records FROM call_records;

SELECT COUNT(*) AS total_invoices FROM invoices;

