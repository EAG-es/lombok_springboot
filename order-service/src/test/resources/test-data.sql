TRUNCATE TABLE orders;
INSERT INTO orders (
        id,
        order_number,
        product_id,
        quantity,
        total_price
    )
VALUES (1, 'ORD-123', 1, 2, 100.00);