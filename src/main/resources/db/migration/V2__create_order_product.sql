CREATE TABLE order_product
(
    id           BINARY(16) PRIMARY KEY,
    order_id     BINARY(16)     NOT NULL,
    product_id   BINARY(16)     NOT NULL,
    product_name VARCHAR(255)   NOT NULL,
    unit_price   DECIMAL(10, 2) NOT NULL,
    quantity     INT            NOT NULL,
    subtotal     DECIMAL(10, 2) NOT NULL,
    created_at   TIMESTAMP      NOT NULL,
    created_by   BINARY(16)     NOT NULL,
    updated_at   TIMESTAMP,
    updated_by   BINARY(16),
    deleted      BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_order_product_order
        FOREIGN KEY (order_id) REFERENCES `order` (id)
);
