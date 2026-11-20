-- ============================================================
-- ASSIGNMENT 7 - SET 3: RETAIL RETURNS AND REFUNDS
-- MySQL 8.0+
-- Complete, standalone script: database + tables + sample data
-- + ten reports + NULL / NOT IN demonstration + ANY / ALL
-- + two nested subqueries.
-- ============================================================

DROP DATABASE IF EXISTS retail_returns_db;
CREATE DATABASE retail_returns_db;
USE retail_returns_db;

-- ------------------------------------------------------------
-- 1. TABLE CREATION
-- ------------------------------------------------------------

CREATE TABLE stores (
    store_id INT PRIMARY KEY,
    store_name VARCHAR(100) NOT NULL,
    region VARCHAR(50) NOT NULL,
    city VARCHAR(60) NOT NULL
);

CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    job_title VARCHAR(60) NOT NULL,
    store_id INT NOT NULL,
    manager_id INT NULL,
    FOREIGN KEY (store_id) REFERENCES stores(store_id),
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE,
    city VARCHAR(60)
);

CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(60) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT NOT NULL,
    store_id INT NOT NULL,
    order_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
);

CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity_sold INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE returns (
    return_id INT PRIMARY KEY,
    order_item_id INT NOT NULL,
    return_date DATE NOT NULL,
    quantity_returned INT NOT NULL,
    return_reason VARCHAR(100) NOT NULL,
    FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id)
);

-- return_id is nullable intentionally. A NULL represents an imported
-- legacy refund adjustment that has no linked return record. It is used
-- to demonstrate why NOT IN can fail when its subquery contains NULL.
CREATE TABLE refunds (
    refund_id INT PRIMARY KEY,
    return_id INT NULL,
    refund_amount DECIMAL(10,2) NOT NULL,
    refund_date DATE NOT NULL,
    FOREIGN KEY (return_id) REFERENCES returns(return_id)
);

-- ------------------------------------------------------------
-- 2. SAMPLE DATA
-- ------------------------------------------------------------

INSERT INTO stores VALUES
(1, 'Coimbatore Central', 'South', 'Coimbatore'),
(2, 'Chennai City',      'South', 'Chennai'),
(3, 'Bengaluru Mall',    'South', 'Bengaluru'),
(4, 'Mumbai Central',    'West',  'Mumbai'),
(5, 'Pune Market',       'West',  'Pune'),
(6, 'Delhi Plaza',       'North', 'Delhi');

INSERT INTO employees VALUES
(101, 'Arun Kumar',  'Store Manager', 1, NULL),
(102, 'Bala',        'Sales Executive',1, 101),
(103, 'Charan',      'Sales Executive',1, 101),
(104, 'Divya',       'Store Manager', 2, NULL),
(105, 'Ezhil',       'Sales Executive',2, 104),
(106, 'Farah',       'Store Manager', 3, NULL);

INSERT INTO customers VALUES
(201, 'Kavya',  'kavya@example.com',  'Coimbatore'),
(202, 'Manoj',  'manoj@example.com',  'Chennai'),
(203, 'Nisha',  'nisha@example.com',  'Bengaluru'),
(204, 'Praveen','praveen@example.com','Mumbai'),
(205, 'Riya',   'riya@example.com',   'Pune'),
(206, 'Sanjay', 'sanjay@example.com', 'Delhi');

INSERT INTO products VALUES
(301, 'Laptop',       'Electronics', 50000.00),
(302, 'Headphones',   'Electronics',  2000.00),
(303, 'Office Chair', 'Furniture',    8000.00),
(304, 'Smart Watch',  'Electronics',  6000.00),
(305, 'Desk Lamp',    'Home',         1500.00);

INSERT INTO orders VALUES
(401, 201, 1, '2026-01-05'),
(402, 202, 1, '2026-01-08'),
(403, 203, 2, '2026-01-10'),
(404, 204, 2, '2026-01-12'),
(405, 205, 3, '2026-01-15'),
(406, 206, 4, '2026-01-18'),
(407, 201, 4, '2026-02-02'),
(408, 202, 5, '2026-02-05'),
(409, 203, 6, '2026-02-08'),
(410, 204, 1, '2026-02-12');

INSERT INTO order_items VALUES
(501, 401, 301, 10, 50000.00),
(502, 401, 302, 20,  2000.00),
(503, 402, 303,  5,  8000.00),
(504, 403, 301,  5, 50000.00),
(505, 404, 304, 10,  6000.00),
(506, 405, 302, 10,  2000.00),
(507, 406, 305, 20,  1500.00),
(508, 407, 301,  5, 50000.00),
(509, 408, 303, 10,  8000.00),
(510, 409, 304,  5,  6000.00),
(511, 410, 302, 10,  2000.00);

INSERT INTO returns VALUES
(601, 501, '2026-01-20', 2, 'Defective'),
(602, 502, '2026-01-21', 3, 'Changed mind'),
(603, 503, '2026-01-22', 1, 'Damaged'),
(604, 504, '2026-01-25', 1, 'Defective'),
(605, 505, '2026-01-28', 2, 'Wrong item'),
(606, 506, '2026-02-01', 2, 'Changed mind'),
(607, 507, '2026-02-03', 1, 'Damaged'),
(608, 508, '2026-02-10', 1, 'Defective'),
(609, 509, '2026-02-12', 2, 'Damaged'),
(610, 510, '2026-02-15', 1, 'Wrong item');

INSERT INTO refunds VALUES
(701, 601, 100000.00, '2026-01-21'),
(702, 602,   6000.00, '2026-01-22'),
(703, 603,   8000.00, '2026-01-23'),
(704, 604,  50000.00, '2026-01-26'),
(705, 605,  12000.00, '2026-01-29'),
(706, 606,   4000.00, '2026-02-02'),
(707, 608,  50000.00, '2026-02-11'),
(708, 609,  16000.00, '2026-02-13'),
(709, NULL,  1500.00, '2026-02-20');

-- ------------------------------------------------------------
-- TASK 1: TEN BUSINESS REPORTS
-- ------------------------------------------------------------

-- Q1. Products with a return rate above 10%.
-- Return rate = total returned quantity / total sold quantity * 100.
SELECT p.product_id, p.product_name,
       SUM(oi.quantity_sold) AS total_quantity_sold,
       COALESCE(SUM(r.quantity_returned), 0) AS total_quantity_returned,
       ROUND(COALESCE(SUM(r.quantity_returned), 0) /
             NULLIF(SUM(oi.quantity_sold), 0) * 100, 2) AS return_rate_percent
FROM products p
JOIN order_items oi ON oi.product_id = p.product_id
LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
GROUP BY p.product_id, p.product_name
HAVING return_rate_percent > 10
ORDER BY return_rate_percent DESC;

-- Q2. Customers who made returns for which no refund was issued.
SELECT DISTINCT c.customer_id, c.customer_name
FROM customers c
JOIN orders o ON o.customer_id = c.customer_id
JOIN order_items oi ON oi.order_id = o.order_id
JOIN returns r ON r.order_item_id = oi.order_item_id
WHERE NOT EXISTS (
    SELECT 1
    FROM refunds f
    WHERE f.return_id = r.return_id
)
ORDER BY c.customer_id;

-- Q3. Store(s) with the highest total refund value.
WITH store_refunds AS (
    SELECT s.store_id, s.store_name,
           COALESCE(SUM(f.refund_amount), 0) AS total_refund_value
    FROM stores s
    LEFT JOIN orders o ON o.store_id = s.store_id
    LEFT JOIN order_items oi ON oi.order_id = o.order_id
    LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
    LEFT JOIN refunds f ON f.return_id = r.return_id
    GROUP BY s.store_id, s.store_name
)
SELECT store_id, store_name, total_refund_value
FROM store_refunds
WHERE total_refund_value = (SELECT MAX(total_refund_value) FROM store_refunds);

-- Q4. Employees and their managers (self join).
SELECT e.employee_id, e.employee_name, e.job_title,
       m.employee_id AS manager_id,
       m.employee_name AS manager_name
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.employee_id
ORDER BY e.employee_id;

-- Q5. Total refund value by month.
SELECT DATE_FORMAT(refund_date, '%Y-%m') AS refund_month,
       COUNT(*) AS refund_transactions,
       SUM(refund_amount) AS total_refund_value
FROM refunds
GROUP BY DATE_FORMAT(refund_date, '%Y-%m')
ORDER BY refund_month;

-- Q6. Products ranked by total returned quantity.
SELECT p.product_id, p.product_name,
       COALESCE(SUM(r.quantity_returned), 0) AS returned_quantity
FROM products p
LEFT JOIN order_items oi ON oi.product_id = p.product_id
LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
GROUP BY p.product_id, p.product_name
ORDER BY returned_quantity DESC, p.product_name;

-- Q7. Customers ranked by total refund amount.
SELECT c.customer_id, c.customer_name,
       COALESCE(SUM(f.refund_amount), 0) AS total_refund_amount
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.customer_id
LEFT JOIN order_items oi ON oi.order_id = o.order_id
LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
LEFT JOIN refunds f ON f.return_id = r.return_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_refund_amount DESC, c.customer_name;

-- Q8. Number of returns by return reason.
SELECT return_reason, COUNT(*) AS number_of_returns,
       SUM(quantity_returned) AS total_units_returned
FROM returns
GROUP BY return_reason
ORDER BY number_of_returns DESC, return_reason;

-- Q9. Store-wise return rate (returned units / sold units).
SELECT s.store_id, s.store_name,
       SUM(oi.quantity_sold) AS units_sold,
       COALESCE(SUM(r.quantity_returned), 0) AS units_returned,
       ROUND(COALESCE(SUM(r.quantity_returned), 0) /
             NULLIF(SUM(oi.quantity_sold), 0) * 100, 2) AS return_rate_percent
FROM stores s
JOIN orders o ON o.store_id = s.store_id
JOIN order_items oi ON oi.order_id = o.order_id
LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
GROUP BY s.store_id, s.store_name
ORDER BY return_rate_percent DESC;

-- Q10. Returns with no refund, including customer, store and product.
SELECT r.return_id, c.customer_name, s.store_name, p.product_name,
       r.quantity_returned, r.return_reason, r.return_date
FROM returns r
JOIN order_items oi ON oi.order_item_id = r.order_item_id
JOIN orders o ON o.order_id = oi.order_id
JOIN customers c ON c.customer_id = o.customer_id
JOIN stores s ON s.store_id = o.store_id
JOIN products p ON p.product_id = oi.product_id
WHERE NOT EXISTS (
    SELECT 1 FROM refunds f WHERE f.return_id = r.return_id
)
ORDER BY r.return_date;

-- ------------------------------------------------------------
-- TASK 2: NOT IN NULL PITFALL AND NOT EXISTS CORRECTION
-- ------------------------------------------------------------

-- Incorrect result: because the subquery contains NULL, the NOT IN
-- predicate evaluates to UNKNOWN for comparisons and returns no rows.
SELECT r.return_id
FROM returns r
WHERE r.return_id NOT IN (
    SELECT f.return_id FROM refunds f
);

-- Correct result: NOT EXISTS handles the NULL row safely.
SELECT r.return_id
FROM returns r
WHERE NOT EXISTS (
    SELECT 1
    FROM refunds f
    WHERE f.return_id = r.return_id
)
ORDER BY r.return_id;

-- A safe NOT IN alternative is to explicitly exclude NULL values.
SELECT r.return_id
FROM returns r
WHERE r.return_id NOT IN (
    SELECT f.return_id
    FROM refunds f
    WHERE f.return_id IS NOT NULL
)
ORDER BY r.return_id;

-- ------------------------------------------------------------
-- TASK 3: ANY AND ALL - COMPARE STORES WITHIN THE SAME REGION
-- ------------------------------------------------------------

-- Store refund totals are calculated first. ANY means greater than
-- at least one other store's refund total in the same region.
WITH store_totals AS (
    SELECT s.store_id, s.store_name, s.region,
           COALESCE(SUM(f.refund_amount), 0) AS refund_total
    FROM stores s
    LEFT JOIN orders o ON o.store_id = s.store_id
    LEFT JOIN order_items oi ON oi.order_id = o.order_id
    LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
    LEFT JOIN refunds f ON f.return_id = r.return_id
    GROUP BY s.store_id, s.store_name, s.region
)
SELECT st.store_id, st.store_name, st.region, st.refund_total
FROM store_totals st
WHERE st.refund_total > ANY (
    SELECT other_store.refund_total
    FROM store_totals other_store
    WHERE other_store.region = st.region
      AND other_store.store_id <> st.store_id
)
ORDER BY st.region, st.refund_total DESC;

-- ALL means greater than every other store's refund total in the same region.
WITH store_totals AS (
    SELECT s.store_id, s.store_name, s.region,
           COALESCE(SUM(f.refund_amount), 0) AS refund_total
    FROM stores s
    LEFT JOIN orders o ON o.store_id = s.store_id
    LEFT JOIN order_items oi ON oi.order_id = o.order_id
    LEFT JOIN returns r ON r.order_item_id = oi.order_item_id
    LEFT JOIN refunds f ON f.return_id = r.return_id
    GROUP BY s.store_id, s.store_name, s.region
)
SELECT st.store_id, st.store_name, st.region, st.refund_total
FROM store_totals st
WHERE st.refund_total > ALL (
    SELECT other_store.refund_total
    FROM store_totals other_store
    WHERE other_store.region = st.region
      AND other_store.store_id <> st.store_id
)
ORDER BY st.region, st.refund_total DESC;

-- ------------------------------------------------------------
-- TASK 4: TWO NESTED SUBQUERIES WITH EXECUTABLE RESULTS
-- ------------------------------------------------------------

-- Nested query 1:
-- Find products whose returned quantity is greater than the average
-- returned quantity across products.
SELECT p.product_id, p.product_name,
       (SELECT COALESCE(SUM(r.quantity_returned), 0)
        FROM order_items oi
        JOIN returns r ON r.order_item_id = oi.order_item_id
        WHERE oi.product_id = p.product_id) AS returned_quantity
FROM products p
WHERE
    (SELECT COALESCE(SUM(r.quantity_returned), 0)
     FROM order_items oi
     JOIN returns r ON r.order_item_id = oi.order_item_id
     WHERE oi.product_id = p.product_id)
    >
    (SELECT AVG(product_return_totals.returned_quantity)
     FROM (
         SELECT p2.product_id,
                COALESCE(SUM(r2.quantity_returned), 0) AS returned_quantity
         FROM products p2
         LEFT JOIN order_items oi2 ON oi2.product_id = p2.product_id
         LEFT JOIN returns r2 ON r2.order_item_id = oi2.order_item_id
         GROUP BY p2.product_id
     ) AS product_return_totals)
ORDER BY returned_quantity DESC;

-- Nested query 2:
-- Find customers whose total refunds are greater than the average
-- total refund amount per customer (customers with no refunds count as 0).
SELECT c.customer_id, c.customer_name,
       COALESCE((
           SELECT SUM(f.refund_amount)
           FROM orders o
           JOIN order_items oi ON oi.order_id = o.order_id
           JOIN returns r ON r.order_item_id = oi.order_item_id
           JOIN refunds f ON f.return_id = r.return_id
           WHERE o.customer_id = c.customer_id
       ), 0) AS customer_refund_total
FROM customers c
WHERE COALESCE((
          SELECT SUM(f.refund_amount)
          FROM orders o
          JOIN order_items oi ON oi.order_id = o.order_id
          JOIN returns r ON r.order_item_id = oi.order_item_id
          JOIN refunds f ON f.return_id = r.return_id
          WHERE o.customer_id = c.customer_id
      ), 0)
      >
      (
          SELECT AVG(customer_totals.refund_total)
          FROM (
              SELECT c2.customer_id,
                     COALESCE(SUM(f2.refund_amount), 0) AS refund_total
              FROM customers c2
              LEFT JOIN orders o2 ON o2.customer_id = c2.customer_id
              LEFT JOIN order_items oi2 ON oi2.order_id = o2.order_id
              LEFT JOIN returns r2 ON r2.order_item_id = oi2.order_item_id
              LEFT JOIN refunds f2 ON f2.return_id = r2.return_id
              GROUP BY c2.customer_id
          ) AS customer_totals
      )
ORDER BY customer_refund_total DESC;

-- ------------------------------------------------------------
-- OPTIONAL VALIDATION: SHOW ALL CREATED TABLES AND ROW COUNTS
-- ------------------------------------------------------------
SELECT 'stores' AS table_name, COUNT(*) AS row_count FROM stores
UNION ALL SELECT 'employees', COUNT(*) FROM employees
UNION ALL SELECT 'customers', COUNT(*) FROM customers
UNION ALL SELECT 'products', COUNT(*) FROM products
UNION ALL SELECT 'orders', COUNT(*) FROM orders
UNION ALL SELECT 'order_items', COUNT(*) FROM order_items
UNION ALL SELECT 'returns', COUNT(*) FROM returns
UNION ALL SELECT 'refunds', COUNT(*) FROM refunds;

-- End of Assignment 7 SQL script.
