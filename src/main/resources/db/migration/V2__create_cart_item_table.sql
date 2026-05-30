CREATE TABLE cart_item
(
    id                BIGINT      NOT NULL,
    status            VARCHAR(20) NOT NULL,
    created_at        DATETIME(6),
    updated_at        DATETIME(6),
    user_id           BIGINT      NOT NULL,
    product_id        BIGINT      NOT NULL,
    product_option_id BIGINT      NOT NULL,
    quantity          INT         NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE INDEX idx_cart_item_lookup ON cart_item (user_id, status);
