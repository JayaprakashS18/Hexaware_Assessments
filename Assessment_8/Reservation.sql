
CREATE DATABASE  RetailInventory;
USE RetailInventory;

-- Drop tables in child-to-parent order so the script can be rerun.
DROP TABLE IF EXISTS OrderItems;
DROP TABLE IF EXISTS Sales;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Categories;

-- ============================================================
-- 1. TABLE CREATION
-- ============================================================

CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    stock_quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    CONSTRAINT chk_products_stock CHECK (stock_quantity >= 0),
    CONSTRAINT chk_products_price CHECK (price >= 0)
) ENGINE = InnoDB;

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12,2) NOT NULL,
    order_status VARCHAR(30) NOT NULL DEFAULT 'RESERVED',
    CONSTRAINT chk_orders_total CHECK (total_amount >= 0)
) ENGINE = InnoDB;

CREATE TABLE OrderItems (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_orderitems_order
        FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    CONSTRAINT fk_orderitems_product
        FOREIGN KEY (product_id) REFERENCES Products(product_id),
    CONSTRAINT chk_orderitems_quantity CHECK (quantity > 0),
    CONSTRAINT chk_orderitems_price CHECK (unit_price >= 0)
) ENGINE = InnoDB;

-- ============================================================
-- 2. SAMPLE DATA
-- ============================================================

INSERT INTO Categories (category_name) VALUES
('Electronics'),
('Clothing'),
('Groceries');

INSERT INTO Products
(product_name, category_id, stock_quantity, price) VALUES
('Laptop',       1, 20, 55000.00),
('Smartphone',   1, 50, 25000.00),
('Headphones',   1, 10,  2000.00),
('T-Shirt',      2, 100,  500.00),
('Jeans',        2, 40,  1500.00),
('Jacket',       2, 15,  2500.00),
('Rice Bag',     3, 80,  1200.00),
('Cooking Oil',  3, 30,   180.00),
('Sugar',        3, 10,    50.00);

-- ============================================================
-- 3. TASK 1: RESERVE STOCK, CREATE ORDER AND ORDER LINE
-- The transaction is all-or-nothing.
-- SELECT ... FOR UPDATE locks the product row while stock is
-- checked and updated, preventing concurrent overselling.
-- ============================================================

DROP PROCEDURE IF EXISTS ReserveOrder;

DELIMITER $$

CREATE PROCEDURE ReserveOrder(
    IN p_customer_name VARCHAR(100),
    IN p_product_id INT,
    IN p_quantity INT
