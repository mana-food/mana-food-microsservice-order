CREATE TABLE `order`
(
    id                       BINARY(16) PRIMARY KEY,
    order_confirmation_time  TIMESTAMP,
    order_status             INT            NOT NULL,
    total_amount             DECIMAL(10, 2) NOT NULL,
    payment_method           INT            NOT NULL,
    created_at               TIMESTAMP      NOT NULL,
    created_by               BINARY(16)     NOT NULL,
    updated_at               TIMESTAMP,
    updated_by               BINARY(16),
    deleted                  BOOLEAN        NOT NULL DEFAULT FALSE,

    INDEX idx_order_status (order_status),
    INDEX idx_order_confirmation_time (order_confirmation_time),
    INDEX idx_created_at (created_at)
);
