
USE RetailInventory;
CREATE TABLE Sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    sale_date DATE NOT NULL,
    quantity_sold INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_sales_product
        FOREIGN KEY (product_id) REFERENCES Products(product_id),
    CONSTRAINT chk_sales_quantity CHECK (quantity_sold > 0),
    CONSTRAINT chk_sales_amount CHECK (total_amount >= 0)
) ENGINE = InnoDB;

INSERT INTO Sales (product_id, sale_date, quantity_sold, total_amount) VALUES
-- January 2026
(1, '2026-01-10',  5, 275000.00),
(2, '2026-01-15', 10, 250000.00),
(3, '2026-01-20',  8,  16000.00),
(4, '2026-01-12', 20,  10000.00),
(5, '2026-01-18', 10,  15000.00),
(6, '2026-01-25',  4,  10000.00),
(7, '2026-01-22', 15,  18000.00),
(8, '2026-01-26', 20,   3600.00),
(9, '2026-01-28', 30,   1500.00),

-- February 2026
(1, '2026-02-10',  8, 440000.00),
(2, '2026-02-15', 12, 300000.00),
(3, '2026-02-20',  5,  10000.00),
(4, '2026-02-12', 25,  12500.00),
(5, '2026-02-18', 15,  22500.00),
(6, '2026-02-25',  6,  15000.00),
(7, '2026-02-22', 20,  24000.00),
(8, '2026-02-26', 25,   4500.00),
(9, '2026-02-28', 20,   1000.00),

-- March 2026
(1, '2026-03-10', 10, 550000.00),
(2, '2026-03-15', 15, 375000.00),
(3, '2026-03-20', 12,  24000.00),
(4, '2026-03-12', 30,  15000.00),
(5, '2026-03-18', 20,  30000.00),
(6, '2026-03-25',  8,  20000.00),
(7, '2026-03-22', 25,  30000.00),
(8, '2026-03-26', 30,   5400.00),
(9, '2026-03-28', 25,   1250.00);

-- ============================================================
-- TASK 3: TOP THREE PRODUCTS BY SALES IN EACH CATEGORY
-- CTE + ROW_NUMBER() + PARTITION BY
-- ============================================================

WITH ProductSales AS (
    SELECT
        p.product_id,
        p.product_name,
        c.category_name,
        SUM(s.quantity_sold) AS total_quantity_sold,
        SUM(s.total_amount) AS total_sales
    FROM Products AS p
    JOIN Categories AS c
      ON c.category_id = p.category_id
    JOIN Sales AS s
      ON s.product_id = p.product_id
    GROUP BY
        p.product_id,
        p.product_name,
        c.category_name
),
RankedProducts AS (
    SELECT
        product_id,
        product_name,
        category_name,
        total_quantity_sold,
        total_sales,
        ROW_NUMBER() OVER (
            PARTITION BY category_name
            ORDER BY total_sales DESC, product_id
        ) AS sales_rank
    FROM ProductSales
