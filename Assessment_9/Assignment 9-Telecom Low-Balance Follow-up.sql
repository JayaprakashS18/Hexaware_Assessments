
-- ============================================================
-- JAVA TRACK - DAY 9 ASSIGNMENT
-- SET 3: TELECOM LOW-BALANCE FOLLOW-UP
-- MYSQL 8.4
-- ============================================================


-- ============================================================
-- STEP 1: CREATE DATABASE
-- ============================================================

DROP DATABASE IF EXISTS telecom_low_balance_db;

CREATE DATABASE telecom_low_balance_db;

USE telecom_low_balance_db;


-- ============================================================
-- STEP 2: CREATE TABLES
-- ============================================================

CREATE TABLE recharge_plans (
    plan_id INT PRIMARY KEY AUTO_INCREMENT,
    plan_name VARCHAR(50) NOT NULL UNIQUE,
    recharge_amount DECIMAL(10,2) NOT NULL,
    validity_days INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE subscribers (
    subscriber_id INT PRIMARY KEY AUTO_INCREMENT,
    subscriber_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100),
    subscriber_type ENUM('PREPAID', 'POSTPAID') NOT NULL,
    plan_id INT,
    balance DECIMAL(10,2) DEFAULT 0.00,
    validity_end_date DATE,

    FOREIGN KEY (plan_id)
        REFERENCES recharge_plans(plan_id)
);


CREATE TABLE recharge_history (
    recharge_id INT PRIMARY KEY AUTO_INCREMENT,
    subscriber_id INT NOT NULL,
    plan_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    recharge_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    balance_after DECIMAL(10,2),

    FOREIGN KEY (subscriber_id)
        REFERENCES subscribers(subscriber_id),

    FOREIGN KEY (plan_id)
        REFERENCES recharge_plans(plan_id)
);


CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    subscriber_id INT NOT NULL,
    notification_message VARCHAR(255),
    notification_type VARCHAR(30),
    notification_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (subscriber_id)
        REFERENCES subscribers(subscriber_id)
);


-- ============================================================
-- STEP 3: INSERT SAMPLE DATA
-- ============================================================

INSERT INTO recharge_plans
(plan_name, recharge_amount, validity_days, is_active)
VALUES
('Basic', 99.00, 28, TRUE),
('Standard', 199.00, 56, TRUE),
('Premium', 399.00, 84, TRUE),
('Inactive Plan', 49.00, 14, FALSE);


INSERT INTO subscribers
(subscriber_name, phone_number, email,
 subscriber_type, plan_id, balance, validity_end_date)
VALUES
('Arun', '9876500001', 'arun@gmail.com',
 'PREPAID', 1, 25.00, DATE_ADD(CURDATE(), INTERVAL 10 DAY)),

('Bala', '9876500002', 'bala@gmail.com',
 'PREPAID', 2, 15.00, DATE_ADD(CURDATE(), INTERVAL 5 DAY)),

('Charan', '9876500003', 'charan@gmail.com',
 'PREPAID', 1, 80.00, DATE_ADD(CURDATE(), INTERVAL 20 DAY)),

('Deepak', '9876500004', 'deepak@gmail.com',
 'PREPAID', 3, 35.00, DATE_ADD(CURDATE(), INTERVAL 15 DAY)),

('Ezhil', '9876500005', 'ezhil@gmail.com',
 'PREPAID', 2, 10.00, DATE_ADD(CURDATE(), INTERVAL 2 DAY)),

('Farhan', '9876500006', 'farhan@gmail.com',
 'PREPAID', 1, 120.00, DATE_ADD(CURDATE(), INTERVAL 25 DAY)),

('Gokul', '9876500007', 'gokul@gmail.com',
 'PREPAID', 3, 45.00, DATE_ADD(CURDATE(), INTERVAL 12 DAY)),

('Hari', '9876500008', 'hari@gmail.com',
 'PREPAID', 1, 5.00, DATE_ADD(CURDATE(), INTERVAL 8 DAY)),

('John', '9876500009', 'john@gmail.com',
 'POSTPAID', 2, 500.00, DATE_ADD(CURDATE(), INTERVAL 30 DAY)),

('Kiran', '9876500010', 'kiran@gmail.com',
 'POSTPAID', 3, 750.00, DATE_ADD(CURDATE(), INTERVAL 40 DAY));


-- ============================================================
-- TASK 1: CREATE VIEWS
-- ============================================================

-- View 1: Prepaid subscribers only.
-- Sensitive contact columns are hidden.

CREATE VIEW vw_prepaid_subscribers AS
SELECT
    s.subscriber_id,
    s.subscriber_name,
    p.plan_name,
    s.balance,
    s.validity_end_date
FROM subscribers s
INNER JOIN recharge_plans p
    ON s.plan_id = p.plan_id
WHERE s.subscriber_type = 'PREPAID';


-- View 2: Recharge summary.
-- Phone number and email are hidden.

CREATE VIEW vw_recharge_summary AS
SELECT
    rh.recharge_id,
    s.subscriber_name,
    p.plan_name,
    rh.amount,
    rh.recharge_date,
    rh.balance_after
FROM recharge_history rh
INNER JOIN subscribers s
    ON rh.subscriber_id = s.subscriber_id
INNER JOIN recharge_plans p
    ON rh.plan_id = p.plan_id;


-- Display views

SELECT * FROM vw_prepaid_subscribers;

SELECT * FROM vw_recharge_summary;


-- ============================================================
-- TASK 2: STORED PROCEDURE - sp_recharge
-- IN AND OUT PARAMETERS
-- TRANSACTION AND ROLLBACK HANDLER
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_recharge(
    IN p_subscriber_id INT,
    IN p_plan_id INT,
    IN p_amount DECIMAL(10,2),
    OUT p_new_balance DECIMAL(10,2)
)
BEGIN

    DECLARE v_subscriber_count INT DEFAULT 0;
    DECLARE v_plan_count INT DEFAULT 0;
    DECLARE v_plan_amount DECIMAL(10,2);
    DECLARE v_validity_days INT;
    DECLARE v_subscriber_type VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SET p_new_balance = NULL;


    -- Validate recharge amount

    IF p_amount IS NULL OR p_amount <= 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid recharge amount';

    END IF;


    -- Check subscriber existence

    SELECT COUNT(*)
    INTO v_subscriber_count
    FROM subscribers
    WHERE subscriber_id = p_subscriber_id;

    IF v_subscriber_count = 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Subscriber does not exist';

    END IF;


    -- Check subscriber type

    SELECT subscriber_type
    INTO v_subscriber_type
    FROM subscribers
    WHERE subscriber_id = p_subscriber_id;

    IF v_subscriber_type <> 'PREPAID' THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Recharge allowed only for prepaid subscribers';

    END IF;


    -- Validate plan

    SELECT COUNT(*)
    INTO v_plan_count
    FROM recharge_plans
    WHERE plan_id = p_plan_id
      AND is_active = TRUE;

    IF v_plan_count = 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid or inactive recharge plan';

    END IF;


    SELECT recharge_amount, validity_days
    INTO v_plan_amount, v_validity_days
    FROM recharge_plans
    WHERE plan_id = p_plan_id
      AND is_active = TRUE;


    -- Validate amount against plan

    IF p_amount <> v_plan_amount THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Recharge amount does not match selected plan';

    END IF;


    -- Update balance and validity

    UPDATE subscribers
    SET
        balance = balance + p_amount,
        plan_id = p_plan_id,
        validity_end_date = DATE_ADD(
            GREATEST(
                COALESCE(validity_end_date, CURDATE()),
                CURDATE()
            ),
            INTERVAL v_validity_days DAY
        )
    WHERE subscriber_id = p_subscriber_id;


    -- Get new balance

    SELECT balance
    INTO p_new_balance
    FROM subscribers
    WHERE subscriber_id = p_subscriber_id;


    -- Insert recharge history

    INSERT INTO recharge_history
    (
        subscriber_id,
        plan_id,
        amount,
        balance_after
    )
    VALUES
    (
        p_subscriber_id,
        p_plan_id,
        p_amount,
        p_new_balance
    );


    COMMIT;

END $$

DELIMITER ;


-- Test stored procedure

SET @new_balance = 0;

CALL sp_recharge(1, 1, 99.00, @new_balance);

SELECT @new_balance AS updated_balance;


-- Verify updated subscriber

SELECT *
FROM subscribers
WHERE subscriber_id = 1;


-- Verify recharge history

SELECT * FROM recharge_history;


-- ============================================================
-- TASK 3: STORED FUNCTION
-- fn_validity_days_left
-- ============================================================

DELIMITER $$

CREATE FUNCTION fn_validity_days_left(
    p_validity_end_date DATE
)
RETURNS INT
NOT DETERMINISTIC
NO SQL
BEGIN

    RETURN DATEDIFF(
        p_validity_end_date,
        CURDATE()
    );

END $$

DELIMITER ;


-- Use function inside SELECT

SELECT
    subscriber_id,
    subscriber_name,
    validity_end_date,
    fn_validity_days_left(validity_end_date)
        AS validity_days_left
FROM subscribers
WHERE subscriber_type = 'PREPAID';


-- ============================================================
-- TASK 4: CURSOR WITH NOT FOUND HANDLER
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_low_balance_cursor()
BEGIN

    DECLARE v_done INT DEFAULT 0;
    DECLARE v_subscriber_id INT;
    DECLARE v_balance DECIMAL(10,2);


    -- Declare cursor

    DECLARE cur_subscribers CURSOR FOR
        SELECT subscriber_id, balance
        FROM subscribers
        WHERE subscriber_type = 'PREPAID'
          AND balance < 50;


    -- NOT FOUND handler

    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET v_done = 1;


    OPEN cur_subscribers;


    read_loop: LOOP

        FETCH cur_subscribers
        INTO v_subscriber_id, v_balance;


        IF v_done = 1 THEN
            LEAVE read_loop;
        END IF;


        -- Insert notification

        INSERT INTO notifications
        (
            subscriber_id,
            notification_message,
            notification_type
        )
        VALUES
        (
            v_subscriber_id,
            CONCAT(
                'Your balance is low. Current balance: ',
                v_balance,
                '. Please recharge.'
            ),
            'LOW_BALANCE'
        );

    END LOOP;


    CLOSE cur_subscribers;

END $$

DELIMITER ;


-- Execute cursor procedure

CALL sp_low_balance_cursor();


-- Display cursor notifications

SELECT * FROM notifications;


-- ============================================================
-- TASK 4: SET-BASED APPROACH
-- INSERT ... SELECT
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_low_balance_set_based()
BEGIN

    INSERT INTO notifications
    (
        subscriber_id,
        notification_message,
        notification_type
    )
    SELECT
        subscriber_id,
        CONCAT(
            'Your balance is low. Current balance: ',
            balance,
            '. Please recharge.'
        ),
        'LOW_BALANCE'
    FROM subscribers
    WHERE subscriber_type = 'PREPAID'
      AND balance < 50;

END $$

DELIMITER ;


-- ============================================================
-- CURSOR VS SET-BASED EXECUTION TIME COMPARISON
-- SAFE UPDATE MODE COMPATIBLE
-- ============================================================


-- Measure cursor execution time

DELETE FROM notifications
WHERE notification_id > 0
  AND notification_type = 'LOW_BALANCE';

SET @cursor_start = NOW(6);

CALL sp_low_balance_cursor();

SET @cursor_end = NOW(6);

SET @cursor_time = TIMESTAMPDIFF(
    MICROSECOND,
    @cursor_start,
    @cursor_end
);


-- Measure set-based execution time

DELETE FROM notifications
WHERE notification_id > 0
  AND notification_type = 'LOW_BALANCE';

SET @set_start = NOW(6);

CALL sp_low_balance_set_based();

SET @set_end = NOW(6);

SET @set_time = TIMESTAMPDIFF(
    MICROSECOND,
    @set_start,
    @set_end
);


-- Display execution time comparison

SELECT
    'Cursor Approach' AS method,
    @cursor_time AS execution_time_microseconds

UNION ALL

SELECT
    'Set-Based Approach',
    @set_time;


-- Display final notifications

SELECT * FROM notifications;


-- ============================================================
-- TASK 5: ERROR HANDLING USING SIGNAL
-- AND SQLEXCEPTION HANDLER
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_test_error_handling(
    IN p_amount DECIMAL(10,2)
)
BEGIN

    DECLARE v_error_message VARCHAR(255);


    -- Handle SQL exceptions

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN

        GET DIAGNOSTICS CONDITION 1
            v_error_message = MESSAGE_TEXT;

        SELECT
            'ERROR HANDLED SUCCESSFULLY' AS status,
            v_error_message AS error_message;

    END;


    -- Raise business error

    IF p_amount IS NULL OR p_amount <= 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Recharge amount must be greater than zero';

    END IF;


    SELECT
        'Recharge amount is valid' AS result;

END $$

DELIMITER ;


-- Test 1: Valid amount

CALL sp_test_error_handling(100);


-- Test 2: Invalid amount

CALL sp_test_error_handling(-100);


-- ============================================================
-- FINAL VERIFICATION
-- ============================================================

-- 1. All subscribers

SELECT * FROM subscribers;


-- 2. All recharge plans

SELECT * FROM recharge_plans;


-- 3. Prepaid subscribers view

SELECT * FROM vw_prepaid_subscribers;


-- 4. Recharge summary view

SELECT * FROM vw_recharge_summary;


-- 5. Recharge history

SELECT * FROM recharge_history;


-- 6. Final notifications

SELECT * FROM notifications;


-- ============================================================
-- END OF DAY 9 ASSIGNMENT - SET 3
-- ============================================================
